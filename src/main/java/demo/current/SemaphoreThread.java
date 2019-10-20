package demo.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreThread {

  private int customer;

  public SemaphoreThread() {
    customer = 0;
  }

  /**
   * ���д�Ǯ��
   */
  class Bank {

    private int account = 100;

    public int getAccount() {
      return account;
    }

    public void save(int money) {
      account += money;
    }
  }

  /**
   * �߳�ִ���࣬ÿ�δ�10��Ǯ
   */
  class NewThread implements Runnable {

    private Bank bank;
    private Semaphore semaphore;

    public NewThread(Bank bank, Semaphore semaphore) {
      this.bank = bank;
      this.semaphore = semaphore;
    }

    @Override
    public void run() {
      int tempCustomer = customer++;
      if (semaphore.availablePermits() > 0) {
        System.out.println("�ͻ�" + tempCustomer + "��������������,��λ������ȥ��Ǯ");
      } else {
        System.out.println("�ͻ�" + tempCustomer + "��������������,��λ�ã�ȥ�Ŷӵȴ��ȴ�");
      }
      try {
        semaphore.acquire();
        bank.save(10);
        System.out.println(tempCustomer + "�������Ϊ��" + bank.getAccount());
        Thread.sleep(1000);
        System.out.println("�ͻ�" + tempCustomer + "��Ǯ��ϣ��뿪����");
        semaphore.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }

  }

  /**
   * �����̣߳������ڲ��࣬��ʼ��Ǯ
   */
  public void useThread() {
    Bank bank = new Bank();
    // ����2���º���
    Semaphore semaphore = new Semaphore(2);
    // ����һ�������̳߳�
    ExecutorService es = Executors.newCachedThreadPool();
    // ����10���߳�
    for (int i = 0; i < 10; i++) {
      // ִ��һ���߳�
      es.submit(new Thread(new NewThread(bank, semaphore)));
    }
    // �ر��̳߳�
    es.shutdown();

    // ���ź����л�ȡ������ɣ������ڻ�����֮ǰ��һֱ���߳�����
    semaphore.acquireUninterruptibly(2);
    System.out.println("�����ˣ�������ԱҪ�Է���");
    // �ͷ�������ɣ������䷵�ظ��ź���
    semaphore.release(2);
  }

  public static void main(String[] args) {
    SemaphoreThread test = new SemaphoreThread();
    test.useThread();
  }
}
