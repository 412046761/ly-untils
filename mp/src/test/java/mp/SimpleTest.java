package mp;

import mp.dao.UserMapper;
import mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description: SimpleTest
 * @author: liyue
 * @date: 2020/9/17 17:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void select(){
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(6,userList.size());
        userList.forEach(System.out::println);
    }
}
