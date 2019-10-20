package demo.current;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TestBlockingQueue {
  /**
   * 生产和消费业务操作
   *
   *
   */
  protected class WorkDesk {

    BlockingQueue<String> desk = new LinkedBlockingQueue<String>(8);

    public void work() throws InterruptedException {
      Thread.sleep(1000);
      desk.put("端出一道菜");
    }

    public String eat() throws InterruptedException {
      Thread.sleep(4000);
      return desk.take();
    }

  }

  /**
   * 生产者类
   *
   *
   */
  class Producer implements Runnable {

    private String producerName;
    private WorkDesk workDesk;

    public Producer(String producerName, WorkDesk workDesk) {
      this.producerName = producerName;
      this.workDesk = workDesk;
    }

    @Override
    public void run() {
      try {
        for (;;) {

          workDesk.work();
          System.out.println(producerName + "端出一道菜" +",Data:"+System.currentTimeMillis());

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 消费者类
   *
   *
   */
  class Consumer implements Runnable {
    private String consumerName;
    private WorkDesk workDesk;

    public Consumer(String consumerName, WorkDesk workDesk) {
      this.consumerName = consumerName;
      this.workDesk = workDesk;
    }

    @Override
    public void run() {
      try {
        for (;;) {
          workDesk.eat();
          System.out.println(consumerName + "端走了一个菜"+",Data:"+System.currentTimeMillis());

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String args[]) throws InterruptedException {
    TestBlockingQueue testQueue = new TestBlockingQueue();
    WorkDesk workDesk = testQueue.new WorkDesk();
    ExecutorService service = Executors.newFixedThreadPool(6);
    //四个生产者线程
    for (int i=1;i<=4;++i) {
      service.submit(testQueue.new Producer("食堂窗口-"+ i+"-", workDesk));
    }

    //两个消费者线程
    Consumer consumer1 = testQueue.new Consumer("顾客-1-", workDesk);
    Consumer consumer2 = testQueue.new Consumer("顾客-2-", workDesk);

    service.submit(consumer1);
    service.submit(consumer2);
    service.shutdown();

  }

}