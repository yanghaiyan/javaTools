package quartz;

import java.util.concurrent.ConcurrentHashMap;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzMgr {

  private static final Logger logger = LoggerFactory.getLogger(QuartzMgr.class);

  private static Scheduler scheduler;
  // ���JobKey

  private static ConcurrentHashMap<String, JobKey> jobs = new ConcurrentHashMap<>();

  // ���TriggerKey
  private static ConcurrentHashMap<String, TriggerKey> triggers = new ConcurrentHashMap<>();

  static {
    try {
      scheduler = StdSchedulerFactory.getDefaultScheduler();
    } catch (SchedulerException e) {
      scheduler = null;
      logger.error("Get Scheduler failed", e);
    }
  }

  // ����scheduler
  public static void start() throws SchedulerException {
    try {
      if (!isStarted()) {
        scheduler.start();
      }

    } catch (SchedulerException e) {
      logger.error("Start Scheduler failed", e);
      throw e;
    }
  }

  // ����scheduler
  public static void shutdown() throws SchedulerException {
    try {
      scheduler.shutdown();
    } catch (SchedulerException e) {
      logger.error("Shutdown Scheduler failed", e);
      throw e;
    }
  }


  /**
   * �ж�Scheduler�Ƿ��Ѿ�����
   */
  public static boolean isStarted() throws SchedulerException {
    try {
      return scheduler.isStarted();
    } catch (SchedulerException e) {
      logger.error("Unable to determine if the Scheduler is started", e);
      throw e;
    }
  }

  // ֹͣ����
  public static void stop(String jobName) throws SchedulerException {
    TriggerKey key = triggers.get(jobName);
    try {
      if (scheduler.checkExists(key)) {
        scheduler.pauseTrigger(key);
        scheduler.unscheduleJob(key);
        delete(jobName);
      }
    } catch (SchedulerException e) {
      logger.error("Job stop failed", e);
      throw e;
    }
  }


  private static void delete(String jobName) {
    JobKey key = jobs.get(jobName);
    try {
      if (scheduler.checkExists(key)) {
        scheduler.deleteJob(key);
      }
    } catch (SchedulerException e) {
      logger.error("delete Job failed", e);
    }
  }

  /**
   * ע������
   *
   * @param jobClazz ������
   * @param jobName ������
   * @param period �������� ��λΪ����
   */
  public static void createScheduleJob(Class<? extends Job> jobClazz, String jobName, int period)
      throws SchedulerException {

    JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName).build();

    Trigger trigger = getSimpleTrigger(jobName, period);

    initScheduleJob(jobDetail, trigger, period);
  }


  public static Trigger getSimpleTrigger(String triggerName, int period) {
    return TriggerBuilder.newTrigger()
        .withIdentity(triggerName)
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMinutes(period)
            .repeatForever()
            // ������ж�Ϊmisfire�˱�������ִ�� �ȴ���һ������
            // misfireThresholdΪĬ������һ���ӳ������ж�Ϊmisfire
            .withMisfireHandlingInstructionNextWithRemainingCount()
        )
        .build();
  }


  private static void initScheduleJob(JobDetail jobDetail, Trigger trigger, int period)
      throws SchedulerException {

    jobs.put(jobDetail.getKey().getName(), jobDetail.getKey());
    triggers.put(trigger.getKey().getName(), trigger.getKey());

    String info = String
        .format("Job name:%s,Job class:%s,Period:%d min", jobDetail.getKey().getName(),
            jobDetail.getJobClass(), period);
    try {
      scheduler.scheduleJob(jobDetail, trigger);
      logger.info("Job registered successfully ,{} ", info);
    } catch (SchedulerException e) {
      logger.error("Job registered failed , " + info, e);
      throw e;
    }
  }
}
