package com.fast.boot.quartz.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * quartz api controller
 *
 * @author: junqing.li
 * @date: 17/5/6
 */
@RestController
@RequestMapping("/quartz")
public class QuartzController {

  @Autowired
  private QuartzManager quartzManager;

  /**
   * 触发一次 任务
   * 
   * @param jobName
   * @param jobGroup
   * @return
   */
  @GetMapping("trigger")
  public String trigger(@RequestParam(value = "jobName") String jobName,
      @RequestParam(value = "jobGroup", required = false,
          defaultValue = JobInfo.DEFAULT_JOB_GROUP) String jobGroup) {

    Assert.hasLength(jobName, "job name is empty");
    Assert.hasLength(jobGroup, "job group is empty");

    if (quartzManager.trigger(jobName, jobGroup)) {
      return "success";
    }
    return "fail";
  }

  public String sechduler(@RequestParam(value = "jobName") String jobName,
      @RequestParam(value = "jobGroup", required = false,
          defaultValue = JobInfo.DEFAULT_JOB_GROUP) String jobGroup) {

    Assert.hasLength(jobName, "job name is empty");
    Assert.hasLength(jobGroup, "job group is empty");



    return null;

  }
}
