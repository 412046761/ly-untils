package mp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import mp.dao.UserMapper;
import mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description: SimpleTest
 * @author: liyue
 * @date: 2020/9/17 17:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void select(){
//        System.out.println("获取全部————————");
//        List<User> userList = getUserList();
//
//        System.out.println("获取全部id = 1087982257332887553 ————————");
//        getUserById(1087982257332887553L);
//
//        System.out.println("获取全部id = 1087982257332887553、1088248166370832385、1088250446457389058 ———————— ");
//        getUserByIds(Arrays.asList(1087982257332887553L,1088248166370832385L,1088250446457389058L));
//
//        System.out.println("条件获取 王天风 25岁 ");
//        Map<String,Object> columnMap = new HashMap<>();
//        columnMap.put("name","王天风");
//        columnMap.put("age","25");
//        getUserListWhere(columnMap);

        /******************
         * answer放的地方 **
         * ****************
         */


    }
    public List<User> getUserList(){
        return userMapper.selectList(null);
    }

    public User getUserById(Long id){
        return userMapper.selectById(id);
    }
    public List<User> getUserByIds(List paramList){
        return userMapper.selectBatchIds(paramList);
    }

    /**
     * map中的Key是数据库中的列名，不是实体类属性名
     * map.put("name","王天风")
     * map.put("age","25")
     * where name ="王天风" and age =30
     * @param map
     * @return
     */
    public List<User> getUserListWhere(Map<String, Object> map){
        return userMapper.selectByMap(map);
    }

    /**
     * 条件查询
     * @return
     */
    public List<User> selectByWrapper(QueryWrapper<User> wrapper){
        return userMapper.selectList(wrapper);
    }

    public void selectLambda(){
        List<User> userList = new LambdaQueryChainWrapper<User>(userMapper).like(User::getName,"雨")
                .ge(User::getAge,20).list();
        userList.forEach(System.out::println);
    }

    @Test
    public void selectMy(){
        LambdaQueryWrapper<User> lambdaQueryChainWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryChainWrapper.like(User::getName,"雨").ge(User::getAge,20);
        List<User> userList = userMapper.selectSql(lambdaQueryChainWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectPage(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>() ;
        queryWrapper.ge("age",26);
        Page<User> page = new Page<User>(1,2);

        IPage<User> ipage = userMapper.selectPage(page,queryWrapper);
        System.out.println("总页数"+ipage.getPages());
        System.out.println("总记录数"+ipage.getTotal());
        List<User> userList = ipage.getRecords();
        userList.forEach(System.out::println);
    }

}
