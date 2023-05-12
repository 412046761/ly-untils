//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.offlinetts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    public Knife4jConfiguration() {
    }

    @Bean({"dockerBean"})
    public Docket dockerBean() {
        Docket docket = (new Docket(DocumentationType.SWAGGER_2)).apiInfo((new ApiInfoBuilder()).description("# Knife4j RESTful APIs").termsOfServiceUrl("https://doc.xiaominfo.com/").version("1.0").build()).groupName("系统管理").select().apis(RequestHandlerSelectors.basePackage("com.example.offlinetts.controller")).paths(PathSelectors.any()).build();
        return docket;
    }
}
