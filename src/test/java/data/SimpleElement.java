package data;

import com.karolk.jioc.annotations.JiocElement;
import com.karolk.jioc.annotations.JiocInject;
import com.karolk.jioc.enums.ElementScope;

import java.util.List;
import java.util.Set;

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

    @JiocInject
    private SimpleElementCustomConstructor simpleElementCustomConstructor;

    @JiocInject(collectionSize = 3)
    private List<SimpleSingleton> simpleSingletonList;

    @JiocInject(collectionSize = 3)
    private List<SimpleSingleton> simpleSingletonList2;

    @JiocInject(collectionSize = 3)
    private List<SimplePrototype> simplePrototypeList;

    @JiocInject(collectionSize = 3)
    private List<SimplePrototype> simplePrototypeList2;

    @JiocInject(collectionSize = 5)
    private Set<SimpleSingleton> simpleSingletonSet;

    @JiocInject(collectionSize = 5)
    private Set<SimplePrototype> simplePrototypeSet;

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

    public SimpleElementCustomConstructor getSimpleElementCustomConstructor() {
        return simpleElementCustomConstructor;
    }

    public List<SimpleSingleton> getSimpleSingletonList() {
        return simpleSingletonList;
    }

    public List<SimplePrototype> getSimplePrototypeList() {
        return simplePrototypeList;
    }

    public List<SimpleSingleton> getSimpleSingletonList2() {
        return simpleSingletonList2;
    }

    public List<SimplePrototype> getSimplePrototypeList2() {
        return simplePrototypeList2;
    }

    public Set<SimpleSingleton> getSimpleSingletonSet() {
        return simpleSingletonSet;
    }

    public Set<SimplePrototype> getSimplePrototypeSet() {
        return simplePrototypeSet;
    }
}