import com.karolk.jioc.context.JiocContext;
import com.karolk.jioc.elements.SimpleElement;
import org.junit.Assert;
import org.junit.Test;

public class IoCTest {

    @Test
    public void shouldReturnDifferentHashCode() {
        JiocContext jiocContext = new JiocContext("com.karolk.jioc.elements");

        SimpleElement simpleElement1 = jiocContext.getElement(SimpleElement.class);
        SimpleElement simpleElement2 = jiocContext.getElement(SimpleElement.class);


        Assert.assertNotEquals(simpleElement1.hashCode(), simpleElement2.hashCode());
    }
}