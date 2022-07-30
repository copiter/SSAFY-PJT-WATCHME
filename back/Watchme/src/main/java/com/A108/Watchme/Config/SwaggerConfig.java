package com.A108.Watchme.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {
        "com.A108.Watchme.Controller"
})
public class SwaggerConfig {

    @Bean
    public Docket WatchmeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("A108")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.A108.Watchme.Controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.WatchmeApiInfo());
//                .tags(   new Tag("AuthController", "Auth API")
//                        , new Tag("MemberController", "Member API")
//                );

    }

    private ApiInfo WatchmeApiInfo() {
        return new ApiInfoBuilder()
                .title("Watchme API")
                .description("Watchme API")
                .termsOfServiceUrl("https://www.watchme2.m??")
                .version("1.0")
                .build();
    }
}