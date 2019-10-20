package demo.current;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class CyclicBarrierWorker implements Runnable {
  private int id;
  private CyclicBarrier barrier;
  public CyclicBarrierWorker(int id, final CyclicBarrier barrier) {
    this.id = id;
    this.barrier = barrier;
  }
  @Override
  public void run() {
    try {
      Thread.sleep(Math.abs(new Random().nextInt()%10000));
      System.out.println(id + " th people wait");
      barrier.await(); // 大家等待最后一个线程到达
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}

public class TestCyclicBarrier {
  public static void main(String[] args) {
    int num = 10;
    CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
      @Override
      public void run() {
        System.out.println("go on together!");
      }
    });
    for (int i = 1; i <= 12; i++) {
      new Thread(new CyclicBarrierWorker(i, barrier)).start();
    }
  }
}