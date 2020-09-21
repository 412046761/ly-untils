
实体类与数据库兼容配置
--------------------------------
##### 1 实体类计算属性，不序列化，不读写库的字段配置方法
方法|说明
---|---
transient|private transient String remark;
static|private static String remark;
@TableField|@TableField(exist = false)
 -|private  String remark;

##### 2.1 实体类的主键不为Id,需要如下配置
```$xslt
  // 主键
  @TableId
  private Long userId;
```

##### 2.2 实体类字段与数据库不一致，需要如下配置
```$xslt
  // 姓名
  @TableField("name")
  private String realName;
```

##### 2.3 实体类名与数据库表名不一致，需要如下配置
```$xslt
  @TableName("mp_user")
  @Data
  public class User {
```

