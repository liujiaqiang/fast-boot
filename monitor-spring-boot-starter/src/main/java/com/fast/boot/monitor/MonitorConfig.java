package com.fast.boot.monitor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author: junqing.li
 * @date: 17/8/16
 */
@Configuration
@ComponentScan(basePackageClasses = Monitor.class)
@EnableAsync(proxyTargetClass = true)
public class MonitorConfig {

}
