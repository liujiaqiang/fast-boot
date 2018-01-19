package com.fast.boot.quartz.starter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.quartz.*;

import com.google.common.collect.Maps;

/**
 *
 * job 集合
 *
 * @author: junqing.li
 * @date: 17/5/7
 */
public class JobContextHodler {

  /** 维持所有的jobDetail **/
  private final static Map<JobKey, JobDetail> jobMap = Maps.newConcurrentMap();

  /** 维持所有的trigger **/
  private final static Map<TriggerKey, Trigger> triggerMap = Maps.newConcurrentMap();

  public static void putJobContext(JobExecutionContext context) {
    // triggerMap.put(trigger.getKey(), trigger);
  }

  public static void removeTrigger(Trigger trigger) {
    if (triggerMap.containsKey(trigger.getKey())) {
      triggerMap.remove(trigger.getKey());
    }
  }

  public static Optional<JobDetailInfo> getByKey(String jobName, String jobGroup) {

    Trigger trigger = triggerMap.get(TriggerKey.triggerKey(jobName, jobGroup));

    if (Objects.isNull(trigger)) {
      return Optional.empty();
    }

    JobDetailInfo jobDetailInfo = new JobDetailInfo();
    jobDetailInfo.setFireTime(trigger.getNextFireTime());
    jobDetailInfo.setNextFireTime(trigger.getNextFireTime());
    // jobDetailInfo.set

    return Optional.of(jobDetailInfo);
  }
}
