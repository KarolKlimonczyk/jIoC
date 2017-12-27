package com.karolk.jioc;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.elements.SimpleElement;

@JiocElement
public class App {

    @JiocInject
    private SimpleElement simpleElement;

    public static void main(String[] args) {
    }

    public SimpleElement getSimpleElement() {
        return simpleElement;
    }
}
