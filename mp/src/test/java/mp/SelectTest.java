package mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mp.dao.UserMapper;
import mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
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
    @Autowired
    private UserMapper userMapper;

    @Test
    public void select(){
//        System.out.println("获取全部————————");
//        getUserList();
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

//        1、名字中包含雨并且年龄小于40
//        name like '%雨%' and age<40
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        queryWrapper.like("name","雨").lt("age",40);
//        selectByWrapper(queryWrapper);

//        2、名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
//        name like '%雨%' and age between 20 and 40 and email is not null
//        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>();
//        queryWrapper2.like("name","雨").between("age",20,40).isNotNull("email");
//        selectByWrapper(queryWrapper2);

//        3、名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
//        name like '王%' or age>=25 order by age desc,id asc
//        QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>();
//        queryWrapper3.likeRight("name","王").or().ge("age",25).orderByAsc("id").orderByDesc("age");
//        selectByWrapper(queryWrapper3);

//        4、创建日期为2019年2月14日并且直属上级为名字为王姓
//        date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
//        QueryWrapper<User> queryWrapper4= new QueryWrapper<User>();
//        queryWrapper4.apply("date_format(create_time,'%Y-%m-%d')={0}","2019-02-14").inSql("manager_id","select id from user where name like '王%'");
//        selectByWrapper(queryWrapper4);

//        5、名字为王姓并且（年龄小于40或邮箱不为空）
//        name like '王%' and (age<40 or email is not null)
//        QueryWrapper<User> queryWrapper5= new QueryWrapper<User>();
//        queryWrapper5.likeRight("name","王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));
//        selectByWrapper(queryWrapper5);

//        6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
//        name like '王%' or (age<40 and age>20 and email is not null)
//        QueryWrapper<User> queryWrapper6= new QueryWrapper<User>();
//        queryWrapper6.likeRight("name","王").or(wq -> wq.lt("age",40).gt("age",20).isNotNull("email"));
//        selectByWrapper(queryWrapper6);

//        7、（年龄小于40或邮箱不为空）并且名字为王姓
//        (age<40 or email is not null) and name like '王%'
//          QueryWrapper<User> queryWrapper7= new QueryWrapper<User>();
//          queryWrapper7.or(wq -> wq.lt("age",40).or().isNotNull("email")).likeRight("name","王");
//          selectByWrapper(queryWrapper7);

//        8、年龄为30、31、34、35
//            age in (30、31、34、35)
//            QueryWrapper<User> queryWrapper8= new QueryWrapper<User>();
//             queryWrapper8.in("age",30,31,34,35);
//            selectByWrapper(queryWrapper8);

//        9、只返回满足条件的其中一条语句即可
//            limit 1
//        QueryWrapper<User> queryWrapper9= new QueryWrapper<User>();
//        queryWrapper9.in("age",30,31,34,35).last(" limit 1");
//        selectByWrapper(queryWrapper9);

//        10、名字中包含雨并且年龄小于40(需求1加强版)
//        第一种情况：select id,name
//        from user
//        where name like '%雨%' and age<40
        QueryWrapper<User> queryWrapper10= new QueryWrapper<User>();
        queryWrapper10.like("name","雨").lt("age",40).select("id","name");
        selectByWrapper(queryWrapper10);

//        第二种情况：select id,name,age,email
//        from user
//        where name like '%雨%' and age<40
//        QueryWrapper<User> queryWrapper100= new QueryWrapper<User>();
//        queryWrapper100.like("name","雨").lt("age",40).select(User.class,info ->
//                !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id") );
//        selectByWrapper(queryWrapper100);

//        11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
//        并且只取年龄总和小于500的组。
//        select avg(age) avg_age,min(age) min_age,max(age) max_age
//        from user
//        group by manager_id
//        having sum(age) <500
        QueryWrapper<User> queryWrapper11= new QueryWrapper<User>();
        queryWrapper11.select("avg(age) avg_age","min(age) min_age","max(age) max_age")
                .groupBy("manager_id").having("sum(age) <{0}",500);
        selectByWrapper(queryWrapper11);


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
}
