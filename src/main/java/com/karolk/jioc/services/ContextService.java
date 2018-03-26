package com.karolk.jioc.services;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementParam;
import com.karolk.jioc.enums.ElementScope;
import com.karolk.jioc.enums.InjectionParam;
import com.karolk.jioc.exceptions.AnnotationParamNotFoundException;
import com.karolk.jioc.exceptions.InvalidConstructorAnnotation;
import com.karolk.jioc.exceptions.NewInstanceConstructException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContextService {

    private final ValidationService validationService;

    public ContextService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public boolean isConditionalScope(Field field) {
        Annotation annotation = field.getAnnotation(JiocInject.class);
        try {
            String conditionalScope = (String) annotation.annotationType().getMethod(InjectionParam.CONDITIONAL_SCOPE).invoke(annotation);
            return ElementScope.PROTOTYPE.equalsIgnoreCase(conditionalScope);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return false;
        }
    }

    public boolean isCollection(Field field) {
        return field.getType().equals(List.class) || field.getType().equals(Set.class);
    }

    public boolean isAnnotated(Class<?> classType) {
        return classType.isAnnotationPresent(JiocElement.class);
    }

    public boolean isInstanceAlreadyExist(Class<?> classType, Map<Class, Object> instances) {
        return instances.containsKey(classType);
    }

    public boolean isSingletonInstance(Class<?> classType) {
        Annotation annotation = classType.getAnnotation(JiocElement.class);
        try {
            String value = (String) annotation.annotationType().getMethod(ElementParam.SCOPE).invoke(annotation);
            return ElementScope.SINGLETON.equalsIgnoreCase(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new AnnotationParamNotFoundException("Annotation parameter: '" + ElementParam.SCOPE + "' not found in a " + classType);
        }
    }

    public Class<?> getFieldType(Field field) {
        if (!this.isCollection(field)) {
            return field.getType();
        }

        Class<?> genericType;
        ParameterizedType genericCollectionType = (ParameterizedType) field.getGenericType();
        genericType = (Class<?>) genericCollectionType.getActualTypeArguments()[0];
        return genericType;
    }

    public boolean isManagedByContainer(Class<?> classType, Field field, Map<Class, Object> instances) {
        return isInstanceAlreadyExist(classType, instances) && isSingletonInstance(classType) && !isConditionalScope(field);
    }

    public Constructor getSuitableConstructor(Class<?> classType) {
        Constructor<?>[] constructors = classType.getDeclaredConstructors();
        List<Constructor<?>> annotatedConstructors = Arrays.stream(constructors).filter(c -> c.isAnnotationPresent(JiocInject.class))
                .collect(Collectors.toList());

        if (annotatedConstructors.size() == 1) {
            this.validationService.validateIfAllConstructorFieldsAreInjectable(annotatedConstructors.get(0), classType);
            return annotatedConstructors.get(0);
        } else if (annotatedConstructors.size() > 1) {
            throw new InvalidConstructorAnnotation("Too many constructors marked with @JiocInject annotation in a " + classType);
        }

        return Arrays.stream(constructors).filter(c -> c.getParameterCount() == 0)
                .findFirst()
                .orElseThrow(() -> new NewInstanceConstructException("Cannot find suitable constructor marked by @JiocInject annotation in a " + classType));
    }
}
