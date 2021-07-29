package com.horseloft.jpa.config;

import com.horseloft.jpa.config.interceptor.ValidateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Date: 2020/1/4 13:55
 * User: YHC
 * Desc: 内容拦截器|接口验证功能等
 *       文档地址：http://localhost:8080/swagger-ui/index.html
 */
@Configuration
public class ContextConfig implements WebMvcConfigurer {

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径可自行配置多个 可用 ，分隔开 .excludePathPatterns("/public/**")
        registry.addInterceptor(this.getValidateInterceptor())
                .excludePathPatterns("/public/**")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/**", "/swagger-ui.html/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
    }

    @Bean
    public ValidateInterceptor getValidateInterceptor() {
        return new ValidateInterceptor();
    }
}
