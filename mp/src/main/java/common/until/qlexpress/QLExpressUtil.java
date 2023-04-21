package common.until.qlexpress;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.Operator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class QLExpressUtil {
    ExpressRunner runner = new ExpressRunner();

    @PostConstruct
    public void init(){
        try {
            runner.addOperator("不在列表",new NotInListOperator());
            runner.addOperator("在列表",new InListOperator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean process(String express,DefaultContext<String,Object> context) throws Exception {
        Object execute = runner.execute(express, context, null, true, false);
        context.clear();
        return (Boolean) execute;
    }

    public static void main(String[] args) throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a",3);
        context.put("b",4);

        runner.addOperator("不在列表",new NotInListOperator());
        runner.addOperator("在列表",new InListOperator());

        Object r = runner.execute("a == a", context, null, false, false);
        System.out.println(r.toString());
    }

}

class NotInListOperator extends Operator {
    @Override
    public Object executeInner(Object[] objects) {
        String value = (String) objects[0];
        Object obj = objects[1];
        if(obj instanceof List){
            List<String> rule = (List<String>) objects[1];
            if(rule.contains(value)){
                return false;
            }
        }else{
           String temp = (String) obj;
           if(temp.equals(value)){
               return false;
           }
        }
        return true;
    }
}

class InListOperator extends Operator {
    @Override
    public Object executeInner(Object[] objects) {
        String value = (String) objects[0];
        Object obj = objects[1];
        if(obj instanceof List){
            List<String> rule = (List<String>) objects[1];
            if(rule.contains(value)){
                return true;
            }
        }else{
            String temp = (String) obj;
            if(temp.equals(value)){
                return true;
            }
        }
        return false;
    }
}
