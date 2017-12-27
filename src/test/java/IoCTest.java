import com.karolk.jioc.context.JiocContext;
import com.karolk.jioc.elements.SimpleElement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IoCTest {

    JiocContext jIocContext;

    @Before
    public void init() {
        jIocContext = new JiocContext("com.karolk.jioc");
    }

    @Test
    public void shouldReturnDifferentHashCode() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);
        //when
        int hashCode1 = simpleElement.getSimplePrototype().hashCode();
        int hashCode2 = simpleElement.getSimplePrototype2().hashCode();

        //then
        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void shouldReturnTheSameHashCode() {
        //given
        SimpleElement simpleElement = jIocContext.getElement(SimpleElement.class);
        //when
        int hashCode1 = simpleElement.getSimpleSingleton().hashCode();
        int hashCode2 = simpleElement.getSimpleSingleton2().hashCode();

        //then
        Assert.assertEquals(hashCode1, hashCode2);
    }
}