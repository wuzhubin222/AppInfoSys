package cn.appsys.tools;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class TestQuartz {
    public static void main(String [] args) throws SchedulerException, InterruptedException {
        //1、创建Job任务,  第一个参数是Job的名字， 第二个参数是Job的所属分组  第三个是具体Job的class
        JobDetail job = new JobDetail("myJob", "A", RemindJob.class);

        //2、创建触发器Trigger。 REPEAT_INDEFINITELY 表示无限期重复. 重复间隔时间为3秒
        SimpleTrigger simTrig = new SimpleTrigger("trigRemindJob", SimpleTrigger.REPEAT_INDEFINITELY, 1000);
        //设置触发器在当前时间的下一秒开始。
        simTrig.setStartTime(new Date(System.currentTimeMillis()+ 1000));

        //3、创建调度器
        //3.1 创建scheduler工厂SchedulerFactory的实例对象

        SchedulerFactory factory = new StdSchedulerFactory();
        //3.2 通过scheduler工厂对象获取调度器
        Scheduler scheduler = factory.getScheduler();
        //4、将任务与触发器进行粘合
        scheduler.scheduleJob(job,simTrig);
        //5、启动调度器。
        scheduler.start();
        Thread.sleep(2000);  //休眠2秒
        //6、停止调度器
        scheduler.shutdown();


    }
    }
