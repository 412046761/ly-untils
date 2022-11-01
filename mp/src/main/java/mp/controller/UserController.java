package mp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mp.entity.User;
import mp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    UserService userService;
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
}
