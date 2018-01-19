package com.fast.boot.quartz.starter.listener;

import com.fast.boot.quartz.starter.JobContextHodler;
import org.quartz.*;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/22.
 *
 * job和trigger监听
 */
@Slf4j
@Getter
@Setter
public class QuartzJobAndTriggerListener implements JobListener, TriggerListener {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

        // log.info("[jobToBeExecuted] {}", JS.toJSON(JobDetailInfo.create(context)));

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

        // log.info("[jobExecutionVetoed] {}", JSON.toJSON(JobDetailInfo.create(context)));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        // log.info("[jobWasExecuted] context={}", JsonUtils.toJSON(JobDetailInfo.create(context)));
    }

    /** --------- trigger --------- **/

    /**
     * trigger的触发
     *
     * @param trigger
     * @param context
     */
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {

        JobContextHodler.putJobContext(context);
    }

    /**
     *
     * Let's say we have a job J and trigger T.
     * 
     * We have a method in TriggerListener vetoJobExecution(). This method is executed when the trigger is just fired.
     * So, with this we can thereby control whether to execute or dismiss the job associated with the trigger. If we
     * want to dismiss the job , then we should return true from this method.
     * 
     * As soon as ,we returned from this method, "jobExecutionVetoed()" method inside our joblistener will be executed
     * to intimate that the job execution has been banned(vetoed).
     *
     * 返回true jobExecutionVetoed将不会执行
     * 
     * @param trigger
     * @param context
     * @return
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {

        return false;

    }

    /**
     * trigger错过触发(mis-fire)
     * 
     * @param trigger
     */
    @Override
    public void triggerMisfired(Trigger trigger) {

        log.info("[triggerMisfired] trigger={}", trigger);

    }

    /**
     * trigger的完成(即trigger触发的job执行完成)
     *
     * @param trigger
     * @param context
     * @param triggerInstructionCode
     */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
        Trigger.CompletedExecutionInstruction triggerInstructionCode) {

        // log.info("[triggerComplete] trigger={} context={} triggerInstructionCode={}", trigger,
        // JsonUtils.toJSON(JobDetailInfo.create(context)), triggerInstructionCode);

        JobContextHodler.putJobContext(context);
    }
}
