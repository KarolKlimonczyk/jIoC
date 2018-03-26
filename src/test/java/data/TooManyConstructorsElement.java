package data;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;

@JiocElement
public class TooManyConstructorsElement {

    private SimplePrototype simplePrototype;
    private SimpleSingleton simpleSingleton;

    @JiocInject
    public TooManyConstructorsElement(SimplePrototype simplePrototype, SimpleSingleton simpleSingleton) {
        this.simplePrototype = simplePrototype;
        this.simpleSingleton = simpleSingleton;
    }

    @JiocInject
    public TooManyConstructorsElement(SimplePrototype simplePrototype) {
        this.simplePrototype = simplePrototype;
    }
}
