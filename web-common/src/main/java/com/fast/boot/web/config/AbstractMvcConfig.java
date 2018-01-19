package com.fast.boot.web.config;

import java.util.List;

import com.fast.boot.web.Interceptor.ContextInterceptor;
import com.fast.boot.web.JsonUtils;
import com.fast.boot.web.exception.DefaultWebExceptionHandler;
import com.fast.boot.web.exception.WebExceptionHandler;
import com.fast.boot.web.handler.ResultHttpMessageConverter;
import com.fast.boot.web.handler.ResultResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by junqing.li on 17/4/18.
 */
@Import(SwaggerConfig.class)
public abstract class AbstractMvcConfig extends WebMvcConfigurerAdapter {


  @Bean
  @ConditionalOnMissingBean(WebExceptionHandler.class)
  public DefaultWebExceptionHandler webExceptionHandler() {
    return new DefaultWebExceptionHandler();
  }

  @Bean
  public ResultResponseBodyAdvice resultResponseBodyAdvice() {
    return new ResultResponseBodyAdvice();
  }

  @Bean
  public ResultHttpMessageConverter resultHttpMessageConverter() {
    ResultHttpMessageConverter converter = new ResultHttpMessageConverter();
    converter.setFastJsonConfig(JsonUtils.fastJsonConfig());
    return converter;
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(new ContextInterceptor());
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    converters.add(resultHttpMessageConverter());
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }



  @Override
  public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

  }

  /**
   * 跨域支持 fuck 没用
   * 
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {

    registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*");
  }
}
