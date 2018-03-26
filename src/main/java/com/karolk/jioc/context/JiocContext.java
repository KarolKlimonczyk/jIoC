package com.karolk.jioc.context;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.exceptions.AnnotationNotFoundException;
import com.karolk.jioc.exceptions.NewInstanceConstructException;
import com.karolk.jioc.services.CollectionsService;
import com.karolk.jioc.services.ContextService;
import com.karolk.jioc.services.ValidationService;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class JiocContext {

    private final Map<Class, Object> instances;
    private final String context;

    private final ContextService contextService;
    private final CollectionsService collectionsService;

    public JiocContext(String context) {
        this.context = context;
        this.instances = new HashMap<>();

        this.contextService = new ContextService(new ValidationService());
        this.collectionsService = new CollectionsService();

        this.init();
    }

    public <T> T getElement(Class<T> classType) {
        return (T) this.resolve(classType, false);
    }

    private void init() {
        Reflections reflections = new Reflections(this.context, new SubTypesScanner(), new FieldAnnotationsScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> elements = reflections.getTypesAnnotatedWith(JiocElement.class);
        elements.forEach(e -> this.resolve(e, false));
    }

    private Object resolve(Class<?> classType, boolean conditionalScope) {

        if (!this.contextService.isAnnotated(classType)) {
            throw new AnnotationNotFoundException(classType + " is not annotated by any jIoC annotation");
        }

        if (this.contextService.isInstanceAlreadyExist(classType, this.instances) && this.contextService.isSingletonInstance(classType) && !conditionalScope) {
            Object instance = this.instances.get(classType);
            this.resolveInjectionForInstanceFields(instance);
            return instance;
        }

        Constructor constructor = this.contextService.getSuitableConstructor(classType);
        Class<?>[] constructorParamsTypes = constructor.getParameterTypes();
        Object[] constructorParams = Arrays.stream(constructorParamsTypes).map(paramTypes -> this.resolve(paramTypes, false)).toArray();

        try {
            Object instance = constructor.newInstance(constructorParams);
            this.resolveInjectionForInstanceFields(instance);
            if (this.contextService.isSingletonInstance(classType)) {
                this.instances.put(classType, instance);
            }
            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NewInstanceConstructException("Cannot resolve dependencies and object cannot be created.");
        }
    }

    private void resolveInjectionForInstanceFields(Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JiocInject.class)) {
                field.setAccessible(true);
                this.resolveInjection(instance, field);
            }
        }
    }

    private Field resolveInjection(Object instance, Field field) {
        Class<?> fieldType = this.contextService.getFieldType(field);
        try {
            Object objectToInject = this.getObjectToInject(field, fieldType);
            field.set(instance, objectToInject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }

    private Object getObjectToInject(Field field, Class<?> genericFieldType) {

        if (this.contextService.isCollection(field)) {
            return this.getCollectionToInject(genericFieldType, field);
        }

        if (this.contextService.isManagedByContainer(field.getType(), field, this.instances)) {
            return this.instances.get(field.getType());
        }

        return this.resolve(field.getType(), this.contextService.isConditionalScope(field));
    }

    private Object getCollectionToInject(Class<?> genericFieldType, Field field) {
        int collectionSize = this.collectionsService.getCollectionSize(field);
        Collection<Object> collection;

        if (field.getType().equals(List.class)) {
            collection = new ArrayList<>();
        } else {
            collection = new HashSet<>();
        }

        for (int i = 0; i < collectionSize; i++) {
            if (this.contextService.isManagedByContainer(genericFieldType, field, this.instances)) {
                collection.add(this.instances.get(genericFieldType));
            } else {
                collection.add(this.resolve(genericFieldType, this.contextService.isConditionalScope(field)));
            }
        }
        return collection;
    }
}
