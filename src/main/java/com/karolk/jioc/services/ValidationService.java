package com.karolk.jioc.services;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.exceptions.InvalidConstructorAnnotation;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ValidationService {

    void validateIfAllConstructorFieldsAreInjectable(Constructor constructor, Class<?> classType) {
        Arrays.stream(constructor.getParameterTypes()).filter(param -> !param.isAnnotationPresent(JiocElement.class))
                .findFirst()
                .ifPresent(param -> {
                    throw new InvalidConstructorAnnotation(param + " is not annotated by @JiocElement annotation and cannot be injected by the constructor in a " + classType);
                });
    }
}
