package pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import netty.entity.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolMgr {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static String threadName = "test";
  private static int coreSize = Runtime.getRuntime().availableProcessors() * 2;
  private static int maxiSize = coreSize * 2;
  private static long timeOut = 2 * 1000;

  private static BlockingQueue queue = new ArrayBlockingQueue(500);

  private static CommonThreadFactory threadFactory = new CommonThreadFactory(threadName);

  private static CommonThreadPool threadPool = null;

  private void init() {
    threadPool = new CommonThreadPool(coreSize, maxiSize, queue, threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy());

  }

  private ThreadPoolMgr() {
    init();
  }

  private static class Inner {

    static ThreadPoolMgr mgr = new ThreadPoolMgr();
  }

  public static ThreadPoolMgr getInstance() {
    return Inner.mgr;
  }

  public ExecutorService getPool() {
    return threadPool;
  }

  public RequestEntity submit(Callable<RequestEntity> task) throws Exception {
    Future<RequestEntity> future = threadPool.submit(task);
    RequestEntity resp = future.get(timeOut, TimeUnit.SECONDS);
    return resp;
  }


  public void start() throws Exception {

    Thread startThread = new Thread() {
      @Override
      public void run() {
        super.run();
        logger.info("log thread pool start...");
        TaskBlockQueue logBlockQueue = TaskBlockQueue.getInstance();
        for (; ; ) {
          try {
            threadPool.execute(logBlockQueue.take());
          } catch (Exception e) {
            logger.error("log thread pool error", e);
          }
        }
      }
    };
    startThread.start();


  }

  public void stop() throws Exception {
    threadPool.shutdown();
  }
}
