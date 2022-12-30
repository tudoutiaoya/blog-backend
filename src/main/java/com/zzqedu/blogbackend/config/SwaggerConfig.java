package com.zzqedu.blogbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) // 用于生成API信息
                .groupName("zzq") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zzqedu.blogbackend.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("博客项目API")
                .description("博客项目SwaggerAPI管理")
                .termsOfServiceUrl("") // 用于定义服务的域名
                .version("1.0")
                .build();
    }

}
