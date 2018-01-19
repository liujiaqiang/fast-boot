package com.fast.boot.quartz.starter;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 支持Autowire spring注入注解
 *
 */
public final class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory
    implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
    final Object job = super.createJobInstance(bundle);
    applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
    return job;
  }

}
