package quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.quartz.SchedulerException;

public class ScheduleServiceMgr {

  private static final ConcurrentMap<String, AbstractScheduleJob> JOBS = new ConcurrentHashMap<>(
      1 << 3);


  private static class Inner {

    private static ScheduleServiceMgr instance = new ScheduleServiceMgr();
  }


  public static ScheduleServiceMgr getInstance() {
    return Inner.instance;
  }

  public List<AbstractScheduleJob> getJobLists() {
    List<AbstractScheduleJob> lists = new ArrayList<>();

    JOBS.entrySet().forEach((job) -> {
      lists.add(job.getValue());
    });

    return lists;
  }


  public void addJob(AbstractScheduleJob job) {
    JOBS.put(job.getJobName(), job);
  }

  public void start(String jobName) throws SchedulerException {
    JOBS.get(jobName).start();
  }

  public void stop(String jobName) throws SchedulerException {
    JOBS.get(jobName).stop();
  }

  public void restart(String jobName) throws SchedulerException {
    stop(jobName);
    start(jobName);
  }
}
