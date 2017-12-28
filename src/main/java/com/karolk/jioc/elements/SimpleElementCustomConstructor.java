package com.karolk.jioc.elements;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;

@JiocElement
public class SimpleElementCustomConstructor {

    private final SimplePrototype simplePrototype;
    private final SimplePrototype simplePrototype2;
    private final SimpleSingleton simpleSingleton;
    private final SimpleSingleton simpleSingleton2;

    @JiocInject
    public SimpleElementCustomConstructor(SimplePrototype simplePrototype, SimplePrototype simplePrototype2, SimpleSingleton simpleSingleton, SimpleSingleton simpleSingleton2) {
        this.simplePrototype = simplePrototype;
        this.simplePrototype2 = simplePrototype2;
        this.simpleSingleton = simpleSingleton;
        this.simpleSingleton2 = simpleSingleton2;
    }

    public SimplePrototype getSimplePrototype() {
        return simplePrototype;
    }

    public SimplePrototype getSimplePrototype2() {
        return simplePrototype2;
    }

    public SimpleSingleton getSimpleSingleton() {
        return simpleSingleton;
    }

    public SimpleSingleton getSimpleSingleton2() {
        return simpleSingleton2;
    }
}
