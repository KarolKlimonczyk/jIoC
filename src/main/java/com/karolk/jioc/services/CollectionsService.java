package com.karolk.jioc.services;

import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.InjectionParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollectionsService {

    private static final Logger LOGGER = Logger.getLogger(CollectionsService.class.getName());

    public int getCollectionSize(Field field) {
        int collectionSize = 0;
        Annotation annotation = field.getAnnotation(JiocInject.class);
        try {
            collectionSize = (int) annotation.annotationType().getMethod(InjectionParam.COLLECTION_SIZE).invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.log(Level.SEVERE, "Cannot obtain CollectionSize from annotation @JiocInject", e.getMessage());
        }
        return collectionSize;
    }
}
