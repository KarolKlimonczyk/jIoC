package com.karolk.jioc.context;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.enums.ElementParam;
import com.karolk.jioc.enums.ElementScope;
import com.karolk.jioc.exceptions.AnnotationNotFoundException;
import com.karolk.jioc.exceptions.AnnotationParamNotFoundException;
import com.karolk.jioc.exceptions.NewInstanceConstructException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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
        Reflections reflections = new Reflections(this.context);
        Set<Class<?>> elements = reflections.getTypesAnnotatedWith(JiocElement.class);
        elements.forEach(this::resolve);
    }

    private Object resolve(Class<?> classType) {

        if (!this.isAnnotated(classType)) {
            throw new AnnotationNotFoundException("Element is not annotated by any jIoC annotation.");
        }

        if (this.isInstanceAlreadyExist(classType) && this.isSingletonInstance(classType)) {
            return this.instances.get(classType);
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
            this.instances.put(classType, instance);
            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new NewInstanceConstructException("Cannot create new instance");
        }
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