# ![Just IoC](https://cdn.pbrd.co/images/H0tzZvs.png)
### Lightweight, fast and easy to use Inversion of Control Container

####About Just Ioc
JIoC allows you to introduce Dependency Injection pattern to your project in a simple way. You can inject objects by fields or by a constructor, all that you have to do is add a proper annotation.

---
####How to use

**@JiocElement**-put this annotation above POJO class declaration, from now JIoC manages this class
```java
@JiocElement
public class SimpleElement {}
```
**Scope parameter**-by default every JIoC elements are singletons if you want to get a new instance every time when an object is injected, change element scope to **prototype**.

```java
@JiocElement(scope = ElementScope.PROTOTYPE)
public class SimpleElement {}
```
**@JiocInject**-put this annotation above field or constructor and let JIoC for injections

```java
@JiocElement
public class SimpleElement {
    @JiocInject
    private Pojo pojo;
}
```

```java
@JiocElement
public class SimpleElement {
    private final Pojo pojo;
    @JiocInject
    public SimpleElement(Pojo pojo) {
        this.pojo = pojo;
    }
}
```


**Inject collections**-if you want to inject collections, just add **collectionSize** parameter to **@JiocInject** annotation and set amount of returned elements, the default size is 1

```java
@JiocElement(scope = ElementScope.PROTOTYPE)
public class SimpleElement {
    @JiocInject(collectionSize = 3)
    private List<Pojo> pojoList;
    @JiocInject(collectionSize = 5)
    private Set<Pojo> pojoSet;
}
```
