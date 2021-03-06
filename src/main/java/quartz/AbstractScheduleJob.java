package quartz;

import java.util.concurrent.atomic.AtomicBoolean;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public abstract class AbstractScheduleJob implements Job {

  private static final Logger logger = LoggerFactory.getLogger(AbstractScheduleJob.class);

  private AtomicBoolean isRunning = new AtomicBoolean(false);

  private void init() throws SchedulerException {
    QuartzMgr.createScheduleJob(this.getClass(),getJobName(),getPeriod());
    isRunning.set(true);
  }

  public abstract String getJobName();

  protected void start() throws SchedulerException {

    if (!isRunning.get()) {
      init();
    }
  }

  protected void stop() {
    if (isRunning.get()) {
      isRunning.set(false);
    }
  }

  public abstract int getPeriod();

  public abstract boolean isEnable();
}
