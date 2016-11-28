package mina.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleJob implements Job{
	int i = 1 ;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Quartz 执行定时任务调度"+(i++));
	}
	
	public static void main(String[] args) {
		try {
			//创建一个JobDetail实例，指定SimpleJob   任务名称，任务组名称，任务实现类
//			JobDetail detail = new JobDetail("testJobDetail", "testGroup", SimpleJob.class);
			
			//通过SimpleTrigger定义调度规则：马上启动，每2秒运行一次，共运行100次  
//			SimpleTrigger trigger = new SimpleTrigger("testSimpleTrigger", "testTrigger");
//			
//			trigger.setStartTime(new Date());//启动开始时间
//			
//			trigger.setRepeatCount(-1);//调度多少次任务
//			
//			trigger.setRepeatInterval(2000);//间隔多久调度一次
			//通过SchedulerFactory获取一个调度器实例
			SchedulerFactory factory = new StdSchedulerFactory();
			
			JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
					.withIdentity("PushEutSmsDao", Scheduler.DEFAULT_GROUP+"_PUSH")
					.requestRecovery(true)
					.build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("PushEutSmsDao", Scheduler.DEFAULT_GROUP+"_PUSH")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule()
							.withIntervalInSeconds(30)
					).build();
			Scheduler scheduler = factory.getScheduler();

			scheduler.scheduleJob(jobDetail, trigger); // 添加到调度管理器中 @注册并进行调度 
			
			//调度启动，@注册并进行调度 任务可先可后，只有注册调度规则和执行的类后，才会去具体执行
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
