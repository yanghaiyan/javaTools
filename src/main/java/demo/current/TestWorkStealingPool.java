package demo.current;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * WorkStealingPool(������ȡ�������ػ��߳�)
 * ÿ���̶߳���Ҫ����Ķ����е�����������е��߳�����Լ������е�����
 * ��ô������ȥ�����߳��л�ȡ�����̵߳�����ȥִ��
 */
public class TestWorkStealingPool {

    public static void main(String[] args) throws IOException {
        // ����cpu�Ǽ��������������߳�
        ForkJoinPool service = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                null, true);

        // �鿴��ǰ������Ǽ���
        System.out.println(Runtime.getRuntime().availableProcessors());
        service.execute(new R(1000));
        service.execute(new R(3000));
        service.execute(new R(4000));
        service.execute(new R(2000));
        service.execute(new R(3000));
        service.execute(new R(3000));
        service.execute(new R(3000));
        service.execute(new R(3000));

        // WorkStealing�Ǿ����߳�(�ػ��̡߳���̨�߳�)�����̲߳������������������
        // �������ֹͣ���ػ��̲߳�ֹͣ
        System.in.read();
    }

    static class R implements Runnable {
        int time;

        public R(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            System.out.println(time + ":" + Thread.currentThread().getName() + "ִ��ʱ��Ϊ��" + System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
