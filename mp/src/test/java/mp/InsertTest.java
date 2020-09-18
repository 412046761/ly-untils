package mp;

import mp.dao.UserMapper;
import mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * @description: InsertTest
 * @author: liyue
 * @date: 2020/9/18 15:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){
        User u = new User();
        u.setRealName("向中");
        u.setAge("13");
        u.setEmail("xq@baomidou.com");
        u.setRemark("备注信息阿斯顿");
        u.setManagerId(1088248166370832385L);
        u.setCreateTime(LocalDateTime.now());
        int rows = userMapper.insert(u);
        System.out.println("插入行数："+rows);
    }
}
