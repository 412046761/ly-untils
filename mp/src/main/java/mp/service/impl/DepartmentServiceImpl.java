package mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mp.entity.Department;
import mp.entity.User;
import mp.service.DepartmentService;
import mp.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author liyue
* @description 针对表【department】的数据库操作Service实现
* @createDate 2022-11-02 14:32:21
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService{
    @Resource
    DepartmentMapper departmentMapper;

}




