//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.offlinetts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OffLineTtsApplication {
    private static final Logger log = LoggerFactory.getLogger(OffLineTtsApplication.class);

    public OffLineTtsApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(OffLineTtsApplication.class, args);
        log.info("\n----------------------------------------------------------\n\tApplication runing-system is running! Access URLs:\n\tSwagger文档: http://localhost:8900/doc.html\n----------------------------------------------------------");
    }
}
