import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * QLExpressTest
 *
 * @author: liyue
 * @date: 2022年11月01日 9:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class QLExpressTest {
    public static void main(String[] args) throws Exception {
        new QLExpressTest().demo();
    }
    /**
     * 测试
     */
    public static void demo() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        String express = "a + b * c";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }
    /**
     * 和java语法相比，要避免的一些ql写法错误
     *
     * 不支持try{}catch{}
     * 不支持单行注释
     * 不支持java8的lambda表达式
     * 不支持for循环集合操作for (Item item : list)
     *
     */
    public static void demo1() throws Exception {

    }

}
