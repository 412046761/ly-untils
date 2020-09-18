#### application.yml 模板,放resources下 （'yourXXX' 需要修改成本地的)
``
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
``