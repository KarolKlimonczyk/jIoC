package com.karolk.jioc.annotations;

import com.karolk.jioc.enums.ElementScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JiocElement {

    String scope() default ElementScope.SINGLETON;
}
