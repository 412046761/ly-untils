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
#### 一对多
##### 1 实体注解 resultMap
```@TableName(value ="department", resultMap = "BaseResultMap")```

##### 2 Mapper.xml 配置 collection 和 子查询
```
<mapper namespace="mp.mapper.DepartmentMapper">

    <resultMap id="BaseResultMap" type="mp.entity.Department">
            <id property="id" column="id" jdbcType="BIGINT"/>
            <result property="name" column="name" jdbcType="VARCHAR"/>
            <result property="code" column="code" jdbcType="VARCHAR"/>
            <result property="orgId" column="org_id" jdbcType="BIGINT"/>
        <collection property="users" column="id" select="findUserByDepartmentId" />
    </resultMap>
    <select id="findUserByDepartmentId" resultType="mp.entity.User">
        select * from user where Department_Id = #{id};
    </select>

</mapper>
```