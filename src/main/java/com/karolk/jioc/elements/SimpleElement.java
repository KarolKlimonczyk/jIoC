package com.karolk.jioc.elements;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementScope;

@JiocElement(scope = ElementScope.PROTOTYPE)
public class SimpleElement {

    //temporarily fields are public (injection not implemented for private fields)

    @JiocInject
    public SimpleSingleton simpleSingleton;

    @JiocInject
    public SimpleSingleton simpleSingleton2;

    @JiocInject
    public SimplePrototype simplePrototype;

    @JiocInject
    public SimplePrototype simplePrototype2;

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