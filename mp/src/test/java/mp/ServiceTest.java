package mp;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sun.javafx.fxml.BeanAdapter;
import mp.entity.User;
import mp.mapper.UserMapper;
import mp.service.UserService;
import mp.until.RedisQueueUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @description: ServiceTest
 * @author: liyue
 * @date: 2020/10/14 15:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
public class ServiceTest {
    @Autowired
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisQueueUtil redisQueue;

    @Test
    public void test(){
        Date date = new Date();

        String day= "2022-01-13";
        try{
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day+" 00:00:00");;
        }catch (Exception e){

        }

        System.out.println(date);

     }
    public static void main(String[] args) {
        new ServiceTest().test();
    }




    public int myAtoi(String s) {
        while (s.startsWith(" ")){
            s = s.substring(1);
        }
        Pattern pattern2 = Pattern.compile("^[0-9]*$|^[-][0-9]*$");
        if(!pattern2.matcher(s).matches()) return 0;
        boolean f = false;
        if(s.substring(0,1).equals("-"))
            f=true;
        String rs = s.replaceAll("[^-(0-9)$]","");
        if(f) rs = "-" + rs;
        long r = Long.valueOf(rs);
        if(r > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        if(r < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        return (int) r;
    }
    public void getOne(){
//        User one =  userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge,28),false);
//        System.out.println(one);
        // 添加
//        redisQueue.rightPush("1",one);
//        // 长度
//        System.out.println(redisQueue.getQueueLength("K111"));
//        // 所有对象
//        List<User> uList = redisQueue.getQueueData("1");
//        uList.forEach(System.out::print);
        //单个对象
//        User u = redisQueue.getPatient("1","1088250446457389058");
//
        // 队长离队
        User u = redisQueue.headLeaveQueue("1");
        System.out.println(u);
    }

    @Test
    public void Batch(){
        User user1 = new User();
        user1.setName("徐丽丽");
        user1.setAge("28");

        User user2 = new User();
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
