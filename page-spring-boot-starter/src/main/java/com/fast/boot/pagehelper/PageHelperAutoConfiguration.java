package com.fast.boot.pagehelper;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.pagehelper.PageInterceptor;

/**
 * 自定注入分页插件
 *
 * @author liuzh
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(PageHelperProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class PageHelperAutoConfiguration implements EnvironmentAware {

  @Autowired
  private SqlSessionFactory sqlSessionFactory;

  @Autowired
  private PageHelperProperties pageHelperProperties;

  private RelaxedPropertyResolver resolver;

  @Override
  public void setEnvironment(Environment environment) {
    resolver = new RelaxedPropertyResolver(environment, "pagehelper.");
  }

  @PostConstruct
  public void addPageInterceptor() {
    PageInterceptor interceptor = new PageInterceptor();
    Properties properties = pageHelperProperties.getProperties();
    Map<String, Object> subProperties = resolver.getSubProperties("");
    for (String key : subProperties.keySet()) {
      if (!properties.containsKey(key)) {
        properties.setProperty(key, resolver.getProperty(key));
      }
    }
    interceptor.setProperties(properties);
    sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
  }

}
