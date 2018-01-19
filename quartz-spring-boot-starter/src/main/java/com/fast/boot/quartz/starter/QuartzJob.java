package com.fast.boot.quartz.starter;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

/**
 * Created by junqing.li on 17/4/20.
 *
 * 註解quartz job
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface QuartzJob {

  /**
   * cron 表达式
   * 
   * @return
   */
  String cron() default "";

  /**
   * job name
   * 
   * @return
   */
  String name() default "";

  /**
   * job group
   * 
   * @return
   */
  String group() default "";

  /**
   * 任务描述
   * 
   * @return
   */
  String desc() default "";

}
