package com.bizseer.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * Function: swagger配置类
 *
 * @author
 * Date: 2018/10/12 下午2:23
 * @since JDK 1.8
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean enabledSwagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabledSwagger)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bizseer.auth.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(
                        Arrays.asList(new ApiKey("Authorization", "Authorization", "header"))
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("auth")
                .description("鉴权系统")
                .version("1.0-SNAPSHOT")
                .build();
    }

}
