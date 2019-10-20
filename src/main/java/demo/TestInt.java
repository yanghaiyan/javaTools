package demo;

public class TestInt {

  public volatile boolean isStop = true;

  public void test() {
    Thread t1 = new Thread() {
      @Override
      public void run() {
        isStop = false;
      }
    };
    Thread t2 = new Thread() {
      @Override
      public void run() {
        while (true) {
          if (isStop){
            System.out.println(1);
            break;
          }
        }
      }
    };
    t1.start();
    t2.start();

  }

  public static void main(String args[]) throws InterruptedException {
    new TestInt().test();
  }
}

