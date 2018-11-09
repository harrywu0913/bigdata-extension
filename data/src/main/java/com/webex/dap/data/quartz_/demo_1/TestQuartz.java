package com.webex.dap.data.quartz_.demo_1;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by harry on 9/18/18.
 */
public class TestQuartz {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        // 创建调度器
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(JobImpl.class).withIdentity("myJob", "jobGroup").build();

        // 创建触发器
        // withIntervalInSeconds(2)表示每隔2s执行任务
        Date triggerDate = new Date();
        SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity("myTrigger", "triggerGroup");
        Trigger trigger = triggerBuilder.startAt(triggerDate).withSchedule(schedBuilder).build();

        // 将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail, trigger);
        // 调度器开始调度任务
        scheduler.start();
    }
}
