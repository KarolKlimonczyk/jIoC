package data;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;

@JiocElement
public class NotElementParam {

    private SimpleSingleton simpleSingleton;
    private SimplePrototype simplePrototype;

    @JiocInject
    public NotElementParam(SimpleSingleton simpleSingleton, SimplePrototype simplePrototype, String value) {
        this.simpleSingleton = simpleSingleton;
        this.simplePrototype = simplePrototype;
    }
}
