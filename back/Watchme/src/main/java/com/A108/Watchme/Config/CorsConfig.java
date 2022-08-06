package com.A108.Watchme.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);
    }
//    @Bean
//    public CorsFilter corsFilter(){
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // 내 서버가 응답을 할 떄 Json을 자바 스크립트에서 활용할 수 있게 설정
//        config.addAllowedOriginPattern("*"); // 모든 ip에 응답을 허용
//        config.addAllowedHeader("*"); // 모든 header에 응답을 허용
//        config.addAllowedMethod("*"); // 모든 Method에 응답을 허용
//        source.registerCorsConfiguration("/**",config);
//        return new CorsFilter(source);
//    }
}
