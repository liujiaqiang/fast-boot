package com.fast.boot.quartz.starter;

import java.util.Date;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: junqing.li
 * @date: 17/5/6
 */
@Slf4j
@Service
public class QuartzManager {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 触发一次任务
     * 
     * @param jobName
     * @param jobGroup
     */
    public Boolean trigger(String jobName, String jobGroup) {

        try {
            schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobName, jobGroup));

            return Boolean.TRUE;

        } catch (SchedulerException e) {

            log.error("[trigger] trigger fail jobName={} jobGroup={} exception={}", jobName, jobGroup, e);
        }
        return Boolean.FALSE;

    }

    public String rescheduleJob(JobInfo jobInfo) {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getName(), jobInfo.getGroup());

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().forJob(jobInfo.getName(), jobInfo.getGroup())
            .withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron())).build();

        try {

            Date date = schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey, cronTrigger);

        } catch (SchedulerException e) {

        }
        return null;
    }
}
