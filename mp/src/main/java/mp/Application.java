package mp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: 启动类
 * @author: liyue
 * @date: 2020/9/17 16:44
 */

@Slf4j
@SpringBootApplication
@MapperScan("mp.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        log.info("\n----------------------------------------------------------\n\t" +
            "Application checkup-sytem is running! Access URLs:\n\t"+
            "Swagger文档: http://localhost:8080/doc.html\n" +
            "----------------------------------------------------------");
    }
}
