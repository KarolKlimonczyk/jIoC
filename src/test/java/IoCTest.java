import com.karolk.jioc.context.JiocContext;
import com.karolk.jioc.elements.SimpleElement;
import com.karolk.jioc.elements.SimpleElementCustomConstructor;
import com.karolk.jioc.elements.SimplePrototype;
import com.karolk.jioc.elements.SimpleSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
}