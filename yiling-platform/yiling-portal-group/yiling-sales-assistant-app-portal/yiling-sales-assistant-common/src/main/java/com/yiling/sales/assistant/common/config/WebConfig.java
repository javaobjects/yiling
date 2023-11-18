package com.yiling.sales.assistant.common.config;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yiling.framework.common.interceptor.TraceIdInterceptor;
import com.yiling.framework.common.json.jackson.DateJacksonConverterConfig;

@Configuration
@Import({ DateJacksonConverterConfig.class })
public class WebConfig implements WebMvcConfigurer {

    /**
     * swagger & knife4j 接口文档相关请求
     */
    public static final String[] apiDocExcludePathPatterns = new String[] { "/**/doc.html", "/**/swagger-ui.html", "/**/swagger-resources/**",
                                                                            "/**/swagger/**", "/**/v2/api-docs", "/**/*.js", "/**/*.css", "/**/*.png",
                                                                            "/**/*.ico", "/**/webjars/springfox-swagger-ui/**", "/**/actuator/**",
                                                                            "/**/druid/**" };
    
    @Resource
    CurrentUserArgumentResolver currentUserArgumentResolver;

    @Bean
    public TraceIdInterceptor traceIdInterceptor() {
        return new TraceIdInterceptor();
    }

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志跟踪标识拦截器
        registry.addInterceptor(this.traceIdInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(apiDocExcludePathPatterns);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
