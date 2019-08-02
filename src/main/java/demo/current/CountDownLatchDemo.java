package demo.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo implements Runnable {


    @Override
    public void run() {

        Thread.currentThread().setName("111111" + Thread.currentThread().getId());
        System.out.println(System.currentTimeMillis() + ": Thread ID:" + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatchDemo demo = new CountDownLatchDemo();
        ExecutorService exec = new ThreadPoolExecutor(10, 15, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>()) {

            @Override
            public void execute(Runnable task) {
                super.execute(warp(task, clientTrace(), Thread.currentThread().getName()));
            }

            private Exception clientTrace() {
                return new Exception("Client stack trace");
            }

            private Runnable warp(final Runnable task, final Exception clientStack, String clientThreadName) {
                return new Runnable() {
                    @Override
                    public void run() {
                        try {
                            task.run();
                        } catch (Exception e) {
                            clientStack.printStackTrace();
                            throw e;
                        }
                    }
                };
            }

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("begin");
            }
        };

        for (int i = 0; i < 10; ++i) {
            exec.execute(demo);
        }
        exec.shutdown();
    }
}
