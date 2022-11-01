import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import mp.entity.User;
import mp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @description: ServiceTest
 * @author: liyue
 * @date: 2020/10/14 15:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void getOne(){
        User one =  userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge,1),false);
        System.out.println(one);
    }

    @Test
    public void Batch(){
        User user1 = new User();
        user1.setName("徐丽丽");
        user1.setAge("28");

        User user2 = new User();
        user2.setId(1316284891526905858L);
        user2.setName("徐大力");
        user2.setAge("30");
        List<User> userList = Arrays.asList(user1,user2);
        boolean saveBatch = userService.saveOrUpdateBatch(userList);
    }

    @Test
    public void getListChain(){
        List<User> userList = userService.lambdaQuery().eq(User::getVersion,1).gt(User::getAge,25).like(User::getName,"雨").list();
        userList.forEach(System.out::print);
    }

    @Test
    public void updateChain(){
       boolean update = userService.lambdaUpdate().eq(User::getAge,13).set(User::getAge,26).update();
       System.out.println(update);
    }

    @Test
    public void removeChain(){
        boolean update = userService.lambdaUpdate().eq(User::getAge,30).remove();
        System.out.println(update);
    }
}
