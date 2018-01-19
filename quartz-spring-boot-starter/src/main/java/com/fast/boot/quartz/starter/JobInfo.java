package com.fast.boot.quartz.starter;

import org.quartz.CronExpression;
import org.quartz.Job;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author junqing.li
 *
 *         Created by junqing.li on 17/4/21.
 */
@Getter
@Setter
public class JobInfo {

    public final static String DEFAULT_JOB_GROUP = "DEFAULT";

    private String cron;

    private String name;

    private String group;

    private String desc;

    public static JobInfo create(QuartzJob quartzJob, Job job) {

        Assert.notNull(quartzJob, "QuartzJob not exsit");
        Assert.isTrue(CronExpression.isValidExpression(quartzJob.cron()), "cron表达式错误");

        JobInfo jobInfo = new JobInfo();
        jobInfo.setCron(quartzJob.cron());
        jobInfo.setDesc(quartzJob.desc());
        if (StringUtils.hasText(quartzJob.group())) {
            jobInfo.setGroup(quartzJob.group());
        }
        // 默认为class简单名
        String jobName = StringUtils.isEmpty(quartzJob.name()) ? job.getClass().getSimpleName() : quartzJob.name();

        jobInfo.setName(jobName);

        return jobInfo;

    }
}
