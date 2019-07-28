package pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TaskBlockQueue {
  public static BlockingQueue<Runnable> blockingQueue = null;

  private TaskBlockQueue() {
    blockingQueue = new ArrayBlockingQueue<Runnable>(500);
  }

  private static class Inner {
    static TaskBlockQueue instance = new TaskBlockQueue();
  }

  public static TaskBlockQueue getInstance() {
    return Inner.instance;
  }


  public void put(Runnable runnable) throws Exception {
    blockingQueue.put(runnable);
  }

  public Runnable take() throws Exception {
    return blockingQueue.take();
  }
}
