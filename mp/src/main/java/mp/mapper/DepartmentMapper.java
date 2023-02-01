package mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mp.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
* @author liyue
* @description 针对表【department】的数据库操作Mapper
* @createDate 2022-11-02 14:32:21
* @Entity mp.entity.Department
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}




