package com.fast.boot.quartz.starter;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 默认有druid采用druid
 * 
 * @author: junqing.li
 * @date: 18/1/6
 */
@ConditionalOnClass(value = {DruidDataSource.class, DruidDataSourceBuilder.class})
@Configurable
public class DataSourceConfig {

    public static final String QUARTZ_DATA_SOURCE = "quartzDataSource";

    @Bean(QUARTZ_DATA_SOURCE)
    @ConfigurationProperties("spring.datasource.druid.quartz")
    public DataSource quartzDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

}
