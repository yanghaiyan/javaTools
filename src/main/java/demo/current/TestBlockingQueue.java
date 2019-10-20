package demo.current;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TestBlockingQueue {
  /**
   * ����������ҵ�����
   *
   *
   */
  protected class WorkDesk {

    BlockingQueue<String> desk = new LinkedBlockingQueue<String>(8);

    public void work() throws InterruptedException {
      Thread.sleep(1000);
      desk.put("�˳�һ����");
    }

    public String eat() throws InterruptedException {
      Thread.sleep(4000);
      return desk.take();
    }

  }

  /**
   * ��������
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
          System.out.println(producerName + "�˳�һ����" +",Data:"+System.currentTimeMillis());

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * ��������
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
          System.out.println(consumerName + "������һ����"+",Data:"+System.currentTimeMillis());

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
    //�ĸ��������߳�
    for (int i=1;i<=4;++i) {
      service.submit(testQueue.new Producer("ʳ�ô���-"+ i+"-", workDesk));
    }

    //�����������߳�
    Consumer consumer1 = testQueue.new Consumer("�˿�-1-", workDesk);
    Consumer consumer2 = testQueue.new Consumer("�˿�-2-", workDesk);

    service.submit(consumer1);
    service.submit(consumer2);
    service.shutdown();

  }

}