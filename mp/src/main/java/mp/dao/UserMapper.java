package mp.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import mp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: UserMapper
 * @author: liyue
 * @date: 2020/9/17 17:16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user ${ew.customSqlSegment}")
    List<User> selectSql(@Param(Constants.WRAPPER) Wrapper<User> wapper);
}
