package mp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: user
 * @author: liyue
 * @date: 2020/9/17 17:09
 */
@Data
public class User {
    // 主键
    private Long Id;
    // 姓名
    private String name;
    // 年龄
    private String age;
    // 邮件
    private String email;
    // 上级Id
    private Long managerId;
    // 创建时间
    private LocalDateTime createTime;

    // 加注释 说明不时表中字段，则不序列化
     @TableField(exist = false)
     private  String remark;
}
