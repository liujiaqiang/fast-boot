package com.fast.boot.quartz.starter;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/21.
 */
@Slf4j
public abstract class AbstractQuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        Stopwatch watch = Stopwatch.createStarted();

        JobInfo jobInfo = getJobInfo();

        log.info("[executeInternal] jobInfo={} startTime={}", jobInfo, new Date());

        execute0(context);

        watch.stop();

        log.info("[executeInternal] jobInfo={} endTime={} jobExecuteTime={} ", getJobInfo(), new Date(),
            watch.toString());

    }

    private JobInfo getJobInfo() {

        QuartzJob quartzJob = AnnotationUtils.findAnnotation(this.getClass(), QuartzJob.class);

        return JobInfo.create(quartzJob, this);

    }

    /**
     * 模板
     *
     * @param context
     */
    public abstract void execute0(JobExecutionContext context);

}
