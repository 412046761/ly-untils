package mp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: user
 * @author: liyue
 * @date: 2020/9/17 17:09
 */
@TableName("mp_user")
@Data
public class User {
    // 主键
    @TableId
    private Long userId;
    // 姓名
    @TableField("name")
    private String realName;
    // 年龄
    private String age;
    // 邮件
    private String email;
    // 上级Id
    private Long managerId;
    // 创建时间
    private LocalDateTime createTime;

    // 备注  (transient不序列化，不入库)
    // private transient String remark;

    // (static 变量不序列化，不入库)
    // private static String remark;

    // 加注释 说明不时表中字段，则不序列化
     @TableField(exist = false)
     private  String remark;

}
