package demo.current;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * WorkStealingPool(任务窃取，都是守护线程)
 * 每个线程都有要处理的队列中的任务，如果其中的线程完成自己队列中的任务，
 * 那么它可以去其他线程中获取其他线程的任务去执行
 */
public class TestWorkStealingPool {

    public static void main(String[] args) throws IOException {
        // 根据cpu是几核来开启几个线程
        ForkJoinPool service = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null, true);

        // 查看当前计算机是几核
        System.out.println(Runtime.getRuntime().availableProcessors());
        service.execute(new R(1000));
        service.execute(new R(3000));
        service.execute(new R(4000));
        service.execute(new R(2000));
        service.execute(new R(3000));
        service.execute(new R(3000));
        service.execute(new R(3000));
        service.execute(new R(3000));

        // WorkStealing是精灵线程(守护线程、后台线程)，主线程不阻塞，看不到输出。
        // 虚拟机不停止，守护线程不停止
        System.in.read();
    }

    static class R implements Runnable {
        int time;

        public R(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            System.out.println(time + ":" + Thread.currentThread().getName() + "执行时间为：" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
