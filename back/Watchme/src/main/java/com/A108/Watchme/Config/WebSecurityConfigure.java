package com.A108.Watchme.Config;

import com.A108.Watchme.Exception.AuthenticationEntryPointHandler;
import com.A108.Watchme.Exception.WebAccessDeniedHandler;
import com.A108.Watchme.auth.CustomOAuth2UserService;
import com.A108.Watchme.auth.OAuth2AuthenticationFailureHandler;
import com.A108.Watchme.auth.OAuth2SuccessHandler;
import com.A108.Watchme.jwt.JwtAuthenticationFilter;
import com.A108.Watchme.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfigure {
    private final CorsFilter corsFilter;
    private final JwtProvider jwtProvider;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final WebAccessDeniedHandler webAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return oAuth2SuccessHandler;
    }
    @Bean
    public AuthenticationFailureHandler oAuth2AuthenticationFailureHandler(){
        return oAuth2AuthenticationFailureHandler;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilter(corsFilter) // @CrossOrigin(인증 X), 시큐리티 필터에 등록해줘야함.
                .authorizeRequests()
                .antMatchers("/api/v2/**","/health","/swagger-ui.html","/swagger/**","/swagger-resources/**","/webjars/**","/v2/api-docs").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/member/**").hasRole("MEMBER")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(webAccessDeniedHandler)
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/slogin")
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler())
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/kakao")
                .and()
                .tokenEndpoint();

        return http.build();
    }

}
