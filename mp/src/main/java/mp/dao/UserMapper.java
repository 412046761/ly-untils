package mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mp.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: UserMapper
 * @author: liyue
 * @date: 2020/9/17 17:16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
