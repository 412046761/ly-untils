package mp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mp.entity.User;
import mp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人员
 */
@Api(tags = "人员管理")
@Slf4j
@RestController
@RequestMapping("web/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 人员列表
     */
    @ApiOperation("查询用户列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public List<User> getUserList(){
        return userService.list();
    }

    /**
     * 人员新增
     */
    @ApiOperation("人员新增")
    @PostMapping(value = "/userAdd")
    public User userAdd(@RequestBody User user){
        userService.save(user);
        return user;
    }


}
