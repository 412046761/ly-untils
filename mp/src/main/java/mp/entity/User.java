package mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;

/**
 * @description: user
 * @author: liyue
 * @date: 2020/9/17 17:09
 */
@Data
@NoArgsConstructor
@TableName(value ="user")
@AllArgsConstructor
@JsonPropertyOrder(alphabetic = true)
public class User {

    // 主键策略
    @TableId(type = IdType.AUTO)
    private String Id;

    // 姓名
    private String name;

    // 年龄
    private String age;

    // 邮件
    private String email;

    // 上级Id
    private Long managerId;

    // 部门id
    private Long departmentId;

    // 创建时间
    @Transient
    //@TableField(fill = FieldFill.INSERT)
    @TableField(exist = false)
    private LocalDateTime createTime;

    // 更新时间
    @Transient
    //@TableField(fill = FieldFill.UPDATE)
    @TableField(exist = false)
    private LocalDateTime updateTime;

    // 版本
    // 记录版本 乐观锁用
    @Version
    private Integer version;

    // 逻辑删除 删除标识（1 已删除）
    @TableLogic
    // 查询不查此字段
    @TableField(select = false)
    private Integer deleted;

    // 加注释
    // 说明不是表中字段，则不序列化
    @TableField(exist = false)
    private  String remark;
}
