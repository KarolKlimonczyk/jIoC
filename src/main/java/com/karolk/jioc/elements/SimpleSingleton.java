package com.karolk.jioc.elements;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;

@JiocElement
public class SimpleSingleton {

    @JiocInject
    public SimplePrototype simplePrototype;

    public SimplePrototype getSimplePrototype() {
        return simplePrototype;
    }
}
