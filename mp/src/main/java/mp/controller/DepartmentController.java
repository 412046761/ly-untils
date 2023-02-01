package mp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mp.entity.Department;
import mp.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门
 */
@Api(tags = "部门管理")
@Slf4j
@RestController
@RequestMapping("web/dep")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    /**
     * 部门列表
     */
    @ApiOperation("查询部门列表")
    @RequestMapping(value = "/getAllDepUser", method = RequestMethod.GET)
    public List<Department> getUserList(){
        return departmentService.list();
    }


}
