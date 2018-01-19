package com.fast.boot.web.config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;

import com.fast.boot.web.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置
 */
@ConditionalOnProperty(prefix = "spring.swagger", name = "enable", havingValue = "true")
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerApiInfo.class)
public class SwaggerConfig {

  @Autowired
  private JsonSerializer jsonSerializer;

  @Autowired
  private SwaggerApiInfo swaggerApiInfo;

  @Bean
  public Docket adminApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .produces(Collections.singleton(MediaType.APPLICATION_JSON_UTF8_VALUE)).apiInfo(apiInfo())
        .select().apis(RequestHandlerSelectors.basePackage(swaggerApiInfo.getBasePackage()))
        .paths(PathSelectors.any()).build();

  }

  private ApiInfo apiInfo() {

    return new ApiInfoBuilder().title(swaggerApiInfo.getTitle())
        .description(swaggerApiInfo.getDescription())
        .termsOfServiceUrl(swaggerApiInfo.getTermsOfServiceUrl())
        .contact(swaggerApiInfo.getContact()).version(swaggerApiInfo.getVersion()).build();

  }

  /**
   * fastjson swagger fix
   * 
   * @return
   */
  @Order(Ordered.LOWEST_PRECEDENCE)
  @Bean
  public FastJsonConfig fastJsonConfig() {
    FastJsonConfig config = JsonUtils.fastJsonConfig();
    config.getSerializeConfig().put(Json.class, new ToStringSerializer() {
      @Override
      public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
          int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
          out.writeNull();
          return;
        }
        out.write(((Json) object).value());
      }
    });
    config.getSerializeConfig().put(UiConfiguration.class, new ToStringSerializer() {
      @Override
      public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
          int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
          out.writeNull();
          return;
        }
        out.write(jsonSerializer.toJson(object).value());
      }
    });
    return config;
  }

  @Bean
  UiConfiguration uiConfig() {
    return new UiConfiguration("validatorUrl", // url
        "none", // docExpansion => none | list
        "alpha", // apiSorter => alpha
        "schema", // defaultModelRendering => schema
        UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, true, // enableJsonEditor => true | false
        true); // showRequestHeaders => true | false
  }



}
