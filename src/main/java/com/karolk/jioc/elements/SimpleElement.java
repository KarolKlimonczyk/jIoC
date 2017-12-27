package com.karolk.jioc.elements;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementScope;

@JiocElement(scope = ElementScope.PROTOTYPE)
public class SimpleElement {

    @JiocInject
    private SimpleSingleton simpleSingleton;

    @JiocInject
    private SimpleSingleton simpleSingleton2;

    @JiocInject
    private SimplePrototype simplePrototype;

    @JiocInject
    private SimplePrototype simplePrototype2;

    public SimpleSingleton getSimpleSingleton() {
        return simpleSingleton;
    }

    public SimpleSingleton getSimpleSingleton2() {
        return simpleSingleton2;
    }

    public SimplePrototype getSimplePrototype() {
        return simplePrototype;
    }

    public SimplePrototype getSimplePrototype2() {
        return simplePrototype2;
    }
}