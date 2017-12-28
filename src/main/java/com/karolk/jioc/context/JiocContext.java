package com.karolk.jioc.context;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementParam;
import com.karolk.jioc.enums.ElementScope;
import com.karolk.jioc.exceptions.*;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
            throw new AnnotationNotFoundException("Element is not annotated by any jIoC annotation");
        }

        if (this.isInstanceAlreadyExist(classType) && this.isSingletonInstance(classType)) {
            Object instance = this.instances.get(classType);
            this.resolveInjectionForInstanceFields(instance);
            return instance;
        }

        Constructor constructor = this.getSuitableConstructor(classType);

        if(constructor == null) {
            throw new SuitableConstructorNotFound("No annotated by @JiocInject or default constructor found in " +classType);
        }

        Class<?>[] constructorParamsTypes = constructor.getParameterTypes();
        Object[] constructorParams = Arrays.stream(constructorParamsTypes).map(this::resolve).toArray();

        try {
            Object instance = constructor.newInstance(constructorParams);
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

    private Constructor getSuitableConstructor(Class<?> classType) {
        Constructor<?>[] constructors = classType.getDeclaredConstructors();
        Constructor annotatedConstructor = null;
        Constructor defaultConstructor = null;

        int amountOfConstructorsMarkedByInjectAnnotation = 0;

        for(Constructor constructor: constructors) {

            if(constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }

            if(constructor.isAnnotationPresent(JiocInject.class)) {
                amountOfConstructorsMarkedByInjectAnnotation++;
                if(amountOfConstructorsMarkedByInjectAnnotation > 1) {
                    throw new InvalidConstructorAnnotation("Too many constructors marked with @JiocInject annotation in " + classType);
                }

                this.validateIfAllConstructorFieldsAreInjectable(constructor);
                annotatedConstructor = constructor;
            }
        }

        if(annotatedConstructor == null) {
            return defaultConstructor;
        }

        return annotatedConstructor;
    }

    private void validateIfAllConstructorFieldsAreInjectable(Constructor constructor) {
        for(Class<?> classType : constructor.getParameterTypes()) {
            if(!classType.isAnnotationPresent(JiocElement.class)) {
                throw new InvalidConstructorAnnotation("Some of the constructor's parameters are not annotated by @JiocElement and cannot be injected in " + classType);
            }
        }
    }
}
