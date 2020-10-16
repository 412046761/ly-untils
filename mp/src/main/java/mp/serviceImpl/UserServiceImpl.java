package mp.serviceImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mp.dao.UserMapper;
import mp.entity.User;
import mp.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @description: UserServiceImpl
 * @author: liyue
 * @date: 2020/10/14 15:15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
