package pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonThreadPool extends ThreadPoolExecutor {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  // 记录开始时间
  private final ThreadLocal<Long> startTimeLocal = new ThreadLocal<>();

  public CommonThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, 15, TimeUnit.SECONDS, workQueue, threadFactory, handler);
  }
  public CommonThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }
  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
    startTimeLocal.set(System.currentTimeMillis());
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    try {
      long endTime = System.currentTimeMillis();
      long taskTime = endTime - startTimeLocal.get();
      logger.info(String.format(" Common thread runtime=%dms", taskTime));
    } finally {
      super.afterExecute(r, t);
    }
  }
}
