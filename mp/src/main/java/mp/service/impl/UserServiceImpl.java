package mp.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mp.mapper.UserMapper;
import mp.entity.User;
import mp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: UserServiceImpl
 * @author: liyue
 * @date: 2020/10/14 15:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
