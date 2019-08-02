package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

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

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(100);

       blockingQueue.put(11);

    List<Integer> list = new ArrayList<>(10);
    blockingQueue.drainTo(list,10);
    System.out.println(list.size());
  }
}
