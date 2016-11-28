package meweb;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class Test implements Job{

	public static void main(String[] args) throws Exception {
		if(args!=null && args.length>0){
			System.out.println("args is "+args[0]);
		}
		System.out.println("quartz job start...");
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		Scheduler  sched = schedFact.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(Test.class)
				.withIdentity("PushRongCloudSmsDao", Scheduler.DEFAULT_GROUP+"_1")
				.requestRecovery(true)
				.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("PushRongCloudSmsDao", Scheduler.DEFAULT_GROUP+"_1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(2).withRepeatCount(10)
				).build();
		sched.scheduleJob(jobDetail, trigger); // 添加到调度管理器中
		sched.start();
		System.out.println("quartz job end...");
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("test jar --- quartz");
	}
}
