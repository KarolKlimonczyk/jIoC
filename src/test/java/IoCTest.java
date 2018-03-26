import com.karolk.jioc.context.JiocContext;
import com.karolk.jioc.exceptions.AnnotationNotFoundException;
import com.karolk.jioc.exceptions.InvalidConstructorAnnotationException;
import com.karolk.jioc.exceptions.NewInstanceConstructException;
import data.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class IoCTest {

    private JiocContext jIocContext;

    @Before
    public void init() {
        jIocContext = new JiocContext("com.karolk.jioc");
    }

    @Test
    public void shouldReturnDifferentObjects() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);
        //when
        SimplePrototype simplePrototype1 = simpleElement.getSimplePrototype();
        SimplePrototype simplePrototype2 = simpleElement.getSimplePrototype2();

        //then
        Assert.assertNotEquals(simplePrototype1, simplePrototype2);
    }

    @Test
    public void shouldReturnTheSameObjects() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);
        //when
        SimpleSingleton simpleSingleton1 = simpleElement.getSimpleSingleton();
        SimpleSingleton simpleSingleton2 = simpleElement.getSimpleSingleton2();

        //then
        Assert.assertEquals(simpleSingleton1, simpleSingleton2);
    }

    @Test
    public void shouldInjectObjectsByConstructor() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);
        SimpleElementCustomConstructor simpleElementCustomConstructor = simpleElement.getSimpleElementCustomConstructor();

        //when
        SimplePrototype simplePrototype1 = simpleElementCustomConstructor.getSimplePrototype();
        SimplePrototype simplePrototype2 = simpleElementCustomConstructor.getSimplePrototype2();
        SimpleSingleton simpleSingleton = simpleElementCustomConstructor.getSimpleSingleton();
        SimpleSingleton simpleSingleton2 = simpleElementCustomConstructor.getSimpleSingleton2();

        //then
        Assert.assertNotNull(simplePrototype1);
        Assert.assertNotNull(simplePrototype2);
        Assert.assertNotNull(simpleSingleton);

        Assert.assertNotEquals(simplePrototype1, simplePrototype2);
        Assert.assertEquals(simpleSingleton, simpleSingleton2);
    }

    @Test
    public void shouldInjectCollections() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);

        //when
        List<SimpleSingleton> simpleSingletonList = simpleElement.getSimpleSingletonList();
        List<SimplePrototype> simplePrototypeList = simpleElement.getSimplePrototypeList();
        Set<SimpleSingleton> simpleSingletonSet = simpleElement.getSimpleSingletonSet();
        Set<SimplePrototype> simplePrototypeSet = simpleElement.getSimplePrototypeSet();

        //then
        Assert.assertNotNull(simpleSingletonList);
        Assert.assertNotNull(simplePrototypeList);
        Assert.assertNotNull(simpleSingletonSet);
        Assert.assertNotNull(simplePrototypeSet);

        Assert.assertEquals(3, simpleSingletonList.size());
        Assert.assertEquals(3, simplePrototypeList.size());
        Assert.assertEquals(1, simpleSingletonSet.size());
        Assert.assertEquals(5, simplePrototypeSet.size());
    }

    @Test
    public void shouldReturnSetWithOneElement() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);

        //when
        Set<SimpleSingleton> simpleSingletonSet = simpleElement.getSimpleSingletonSet();

        //then
        Assert.assertEquals(1, simpleSingletonSet.size());
    }

    @Test
    public void shouldReturnListOfSingletons() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);

        //when
        List<SimpleSingleton> simpleSingletonList = simpleElement.getSimpleSingletonList();

        //then
        Assert.assertEquals(simpleSingletonList.get(0), simpleSingletonList.get(1));
        Assert.assertEquals(simpleSingletonList.get(0), simpleSingletonList.get(2));
        Assert.assertEquals(simpleSingletonList.get(1), simpleSingletonList.get(2));
    }

    @Test
    public void shouldReturnListOfPrototypes() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);

        //when
        List<SimplePrototype> simplePrototypeList = simpleElement.getSimplePrototypeList();

        //then
        Assert.assertNotEquals(simplePrototypeList.get(0), simplePrototypeList.get(1));
        Assert.assertNotEquals(simplePrototypeList.get(0), simplePrototypeList.get(2));
        Assert.assertNotEquals(simplePrototypeList.get(1), simplePrototypeList.get(2));
    }

    @Test
    public void shouldReturnPrototypeFromConditionalScope() {
        //given
        SimpleConditionalScope simpleConditionalScope = jIocContext.getElement(SimpleConditionalScope.class);

        //then
        Assert.assertEquals(simpleConditionalScope.getSimpleSingleton1(), simpleConditionalScope.getSimpleSingleton2());
        Assert.assertNotEquals(simpleConditionalScope.getSimpleSingleton1(), simpleConditionalScope.getConditionalElement1());
        Assert.assertNotEquals(simpleConditionalScope.getSimpleSingleton1(), simpleConditionalScope.getConditionalElement1());
        Assert.assertNotEquals(simpleConditionalScope.getConditionalElement1(), simpleConditionalScope.getConditionalElement2());
    }

    @Test(expected = InvalidConstructorAnnotationException.class)
    public void shouldThrowInvalidConstructorAnnotationException() {
        //when
        jIocContext.getElement(TooManyConstructorsElement.class);
    }

    @Test(expected = NewInstanceConstructException.class)
    public void shouldThrowNewInstanceConstructException() {
        //when
        jIocContext.getElement(NoSuitableConstructorElement.class);
    }

    @Test(expected = InvalidConstructorAnnotationException.class)
    public void shouldThrowInvalidConstructorAnnotationExceptionWhenNoElementParam() {
        //when
        jIocContext.getElement(NotElementParam.class);
    }

    @Test(expected = AnnotationNotFoundException.class)
    public void shouldThrowAnnotationNotFoundException() {
        //when
        jIocContext.getElement(NotAnnotatedElement.class);
    }

}