package com.karolk.jioc.context;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementParam;
import com.karolk.jioc.enums.ElementScope;
import com.karolk.jioc.exceptions.AnnotationNotFoundException;
import com.karolk.jioc.exceptions.AnnotationParamNotFoundException;
import com.karolk.jioc.exceptions.NewInstanceConstructException;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JiocContext {

    private final Map<Class, Object> instances;
    private final String context;

    public JiocContext(String context) {
        this.instances = new HashMap<>();
        this.context = context;

        this.init();
    }

    public <T> T getElement(Class<T> classType) {
        return (T) this.resolve(classType);
    }

    private void init() {
        Reflections reflections = new Reflections(this.context, new SubTypesScanner(), new FieldAnnotationsScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> elements = reflections.getTypesAnnotatedWith(JiocElement.class);
        elements.forEach(this::resolve);
    }

    private Object resolve(Class<?> classType) {

        if (!this.isAnnotated(classType)) {
            throw new AnnotationNotFoundException("Element is not annotated by any jIoC annotation.");
        }

        if (this.isInstanceAlreadyExist(classType) && this.isSingletonInstance(classType)) {
            Object instance = this.instances.get(classType);
            this.resolveInjectionForInstanceFields(instance);
            return instance;
        }

        Constructor<?>[] constructors = classType.getConstructors();

        //temporary solution
        if (constructors.length < 1) {
            throw new IllegalStateException("Constructor not found");
        }

        Constructor<?> primaryConstructor = constructors[0];
        Class<?>[] constructorParamsTypes = primaryConstructor.getParameterTypes();
        Object[] constructorParams = Arrays.stream(constructorParamsTypes).map(this::resolve).toArray();

        try {
            Object instance = primaryConstructor.newInstance(constructorParams);
            this.resolveInjectionForInstanceFields(instance);
            this.instances.put(classType, instance);
            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new NewInstanceConstructException("Cannot create new instance");
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

        if (this.isInstanceAlreadyExist(field.getType()) && this.isSingletonInstance(field.getType())) {
            try {
                field.set(instance, this.instances.get(field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return field;
        }

        Object objectToInject = this.resolve(field.getType());
        try {
            field.set(instance, objectToInject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }

    private boolean isAnnotated(Class<?> classType) {
        return classType.isAnnotationPresent(JiocElement.class);
    }

    private boolean isInstanceAlreadyExist(Class<?> classType) {
        return this.instances.containsKey(classType);
    }

    private boolean isSingletonInstance(Class<?> classType) {
        Annotation annotation = classType.getAnnotation(JiocElement.class);
        try {
            String value = (String) annotation.annotationType().getMethod(ElementParam.SCOPE).invoke(annotation);
            return ElementScope.SINGLETON.equalsIgnoreCase(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new AnnotationParamNotFoundException("Annotation param: " + ElementParam.SCOPE + " not found.");
        }
    }
}
