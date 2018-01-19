package com.fast.boot.quartz.starter;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by junqing.li on 17/4/22.
 */
@Getter
@Setter
public class JobDetailInfo extends JobInfo {

  private Date nextFireTime;

  private Date previousFireTime;

  private Date fireTime;

  private Class<? extends Job> jobClass;

  private Trigger.TriggerState triggerState;

  private Long runTime;

  public static JobDetailInfo create(JobExecutionContext context) {

    JobDetailInfo jobDetailInfo = new JobDetailInfo();
    JobDetail jobDetail = context.getJobDetail();
    jobDetailInfo.setName(jobDetail.getKey().getName());
    jobDetailInfo.setGroup(jobDetail.getKey().getGroup());
    jobDetailInfo.setJobClass(jobDetail.getJobClass());
    jobDetailInfo.setDesc(jobDetail.getDescription());

    jobDetailInfo.setPreviousFireTime(context.getPreviousFireTime());
    jobDetailInfo.setNextFireTime(context.getNextFireTime());
    jobDetailInfo.setFireTime(context.getFireTime());
    jobDetailInfo.setRunTime(context.getJobRunTime());
    return jobDetailInfo;
  }

}
