package mp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mp.entity.User;
import mp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import mp.until.RedisQueueUtil;

import java.util.List;

/**
 * 人员
 *
 * @author: liyue
 * @date: 2022年11月01日 15:04
 */
@Api(tags = "人员管理")
@Slf4j
@RestController
@RequestMapping("web/user")
public class UserController {
    @Autowired
    private UserService userService;
//    @Autowired
//    private RedisQueueUtil redisQueue;
    /**
     * 人员列表
     *
     * @Param: []
     * @Return: java.util.List<mp.entity.User>
     * @Author: liyue
     * @Date: 2022/11/1 15:06
     */
    @ApiOperation("查询用户列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public List<User> getUserList(){
        return userService.list();
    }

    /**
     * 人员新增
     *
     * @Param: [user]
     * @Return: mp.entity.User
     * @Author: liyue
     * @Date: 2022/11/1 17:15
     */
    @ApiOperation("人员新增")
    @PostMapping(value = "/userAdd")
    public User userAdd(@RequestBody User user){
        return userAdd(user);
    }
}
