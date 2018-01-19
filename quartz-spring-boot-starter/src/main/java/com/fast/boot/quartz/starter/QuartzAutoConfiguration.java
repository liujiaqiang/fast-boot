package com.fast.boot.quartz.starter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.google.common.collect.Lists;
import com.fast.boot.quartz.starter.listener.QuartzJobAndTriggerListener;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by junqing.li on 17/4/20.
 */
@Import(DataSourceConfig.class)
@ConditionalOnProperty(prefix = "quartz", name = "enable", matchIfMissing = true)
@Configurable
@ComponentScan(basePackageClasses = QuartzAutoConfiguration.class)
public class QuartzAutoConfiguration {

    @Value("${quartz.cluster.scheduler.name}")
    private String schedulerClusterName;

    /**
     * 是否自动启动 默认true
     */
    @Value("${quartz.auto.start.up:true}")
    private boolean autoStartUp;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(DataSourceConfig.QUARTZ_DATA_SOURCE)
    private DataSource quartzDataSource;

    @Bean
    public AutoWiringSpringBeanJobFactory jobFactory() {

        AutoWiringSpringBeanJobFactory factory = new AutoWiringSpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);

        return factory;
    }

    @Bean
    public QuartzJobAndTriggerListener quartzJobAndTriggerListener() {
        QuartzJobAndTriggerListener listener = new QuartzJobAndTriggerListener();
        return listener;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(QuartzJobAndTriggerListener quartzJobAndTriggerListener) {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setApplicationContext(applicationContext);
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setConfigLocation(applicationContext.getResource("classpath:quartz.properties"));

        schedulerFactoryBean.setGlobalJobListeners(quartzJobAndTriggerListener);
        schedulerFactoryBean.setGlobalTriggerListeners(quartzJobAndTriggerListener);

        schedulerFactoryBean.setJobFactory(jobFactory());
        schedulerFactoryBean.setBeanName(schedulerClusterName);
        Holder holder = getQuartzHolder();
        schedulerFactoryBean.setAutoStartup(autoStartUp);
        schedulerFactoryBean.setJobDetails(holder.jobDetailList.toArray(new JobDetail[holder.jobDetailList.size()]));
        schedulerFactoryBean.setTriggers(holder.cronTriggerList.toArray(new Trigger[holder.cronTriggerList.size()]));
        return schedulerFactoryBean;

    }

    /**
     * 获取所有的 job实例
     *
     * @return
     */
    public Holder getQuartzHolder() {

        Map<String, Job> quartzJobMap = applicationContext.getBeansOfType(Job.class);

        Set<String> quartNameSet = quartzJobMap.keySet();

        List<CronTrigger> triggerList = Lists.newArrayList();
        List<JobDetail> jobDetailList = Lists.newArrayList();

        for (String quartName : quartNameSet) {

            Job job = quartzJobMap.get(quartName);
            QuartzJob quartzJob = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
            JobInfo jobInfo = JobInfo.create(quartzJob, job);

            JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withDescription(jobInfo.getDesc())
                .storeDurably(true).withIdentity(jobInfo.getName(), jobInfo.getGroup()).build();
            CronTrigger cronTrigger
                = TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobInfo.getName(), jobInfo.getGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron())).build();

            jobDetailList.add(jobDetail);
            triggerList.add(cronTrigger);
        }

        return new Holder(triggerList, jobDetailList);
    }

    @Data
    @AllArgsConstructor
    public static class Holder {

        private List<CronTrigger> cronTriggerList;

        private List<JobDetail> jobDetailList;

    }
}
