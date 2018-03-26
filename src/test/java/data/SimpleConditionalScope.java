package data;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementScope;

@JiocElement
public class SimpleConditionalScope {

    @JiocInject
    private SimpleSingleton simpleSingleton1;

    @JiocInject
    private SimpleSingleton simpleSingleton2;

    @JiocInject(conditionalScope = ElementScope.PROTOTYPE)
    private SimpleSingleton conditionalElement1;

    @JiocInject(conditionalScope = ElementScope.PROTOTYPE)
    private SimpleSingleton conditionalElement2;

    public SimpleSingleton getSimpleSingleton1() {
        return simpleSingleton1;
    }

    public SimpleSingleton getSimpleSingleton2() {
        return simpleSingleton2;
    }

    public SimpleSingleton getConditionalElement1() {
        return conditionalElement1;
    }

    public SimpleSingleton getConditionalElement2() {
        return conditionalElement2;
    }
}
