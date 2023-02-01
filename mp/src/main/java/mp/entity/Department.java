package mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @TableName department
 */
@Data
@TableName(value ="department", resultMap = "BaseResultMap")
public class Department implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 所属组织
     */
    private Long orgId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 人员
     */
    private List<User> users;
}