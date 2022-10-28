#### application.yml 模板,放resources下 
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://*yourUrl*:3306/yourDatebaseName?useUnicode=true&characterEncoding=UTF-8&&useSSL=true&serverTimezone=UTC
    username: yourName
    password: yourPwd
    initialSize: 5
    minIdle: 5
    maxActive: 20

logging:
  level:
    yourName: warn
    mp.dao: trace
  pattern:
    console: '%p%m%n'
```

示例表SQL

```$xslt
#创建用户表
CREATE TABLE user (
    id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
    name VARCHAR(30) DEFAULT NULL COMMENT '姓名',
    age INT(11) DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    manager_id BIGINT(20) DEFAULT NULL COMMENT '直属上级id',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    CONSTRAINT manager_fk FOREIGN KEY (manager_id)
        REFERENCES user (id)
)  ENGINE=INNODB CHARSET=UTF8;

#初始化数据：
INSERT INTO user (id, name, age, email, manager_id
	, create_time)
VALUES (1087982257332887553, '大boss', 40, 'boss@baomidou.com', NULL
		, '2019-01-11 14:20:20'),
	(1088248166370832385, '王天风', 25, 'wtf@baomidou.com', 1087982257332887553
		, '2019-02-05 11:12:22'),
	(1088250446457389058, '李艺伟', 28, 'lyw@baomidou.com', 1088248166370832385
		, '2019-02-14 08:31:16'),
	(1094590409767661570, '张雨琪', 31, 'zjq@baomidou.com', 1088248166370832385
		, '2019-01-14 09:15:15'),
	(1094592041087729666, '刘红雨', 32, 'lhm@baomidou.com', 1088248166370832385
		, '2019-01-14 09:48:16');
```


---------------------

#### 实体类与数据库兼容配置
--------------------------------
##### 1 实体类计算属性，不序列化，不读写库的字段配置方法
方法|说明
---|---
transient|private transient String remark;
static|private static String remark;
@TableField|@TableField(exist = false)
 -|private  String remark;
##### 2 实体类的主键策略
```aidl

// 1.局部策略，类主键上配置
@TableId(type = IdType.AUTO)

// 2.全局策略，配置文件全局配置
mybatis-plus:
  mapper-locations:
    global-config:
      db-config:
        id-type: uuid

```
策略|说明
---|---
AUTO(0)| 自增
NONE(1)| 跟随全局
INPUT(2)| 手动传入
ID_WORKER(3)| 默认雪花 （数字类型）
UUID(4)| UUID生成 String类型
ID_WORKER_STR(5)| 雪花String类型

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
mapper类不需要加注解托管给spring容器吗?
```aidl
需要，下面任选一种解决：
1.全局配置： 在启动类上加@MapperScan
2.局部配置：Mapper类上加@Mapper注解
```