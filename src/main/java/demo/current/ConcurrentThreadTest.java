package demo.current;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.*;


public class ConcurrentThreadTest {
    //List 任务
    //每次取制定个数任务
    //任务扩展callable
    //指定线程池执行任务 200ms内没有执行完毕的下次不再提取
    //循环执行

    private static Stack<TaskWithId> stack = new Stack<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
    private static int countDownNumber = 3;


    public static void main(String[] args) {

        moskListTask();

        while (true) {
            List<Integer> doneList = new ArrayList<Integer>();
            CountDownLatch latch = new CountDownLatch(countDownNumber);
            List<TaskWithId> taskWithIds = pullFixNumTask(countDownNumber);

            StringBuilder bi = new StringBuilder();
            for (TaskWithId taskWithId : taskWithIds) {
                bi.append(taskWithId.id).append(" ");
            }
            System.out.println("本次需要执行的任务 ids = " + bi.toString());

            if (taskWithIds.isEmpty()) {
                break;
            }
            List<Future<Integer>> result = new ArrayList<Future<Integer>>();
            for (TaskWithId taskWithId : taskWithIds) {
                taskWithId.setLatch(latch);
                result.add(pool.submit(taskWithId));
            }
            //等待latch 设置过期时间
            Uninterruptibles.awaitUninterruptibly(latch, 20, TimeUnit.MICROSECONDS);

            //每个任务等待指定时间 不中断实际任务的执行 获取结果
            for (Future<Integer> future : result) {
                try {
                    Uninterruptibles.getUninterruptibly(future, 1, TimeUnit.SECONDS);//非中断获取结果
                    doneList.add(future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    System.out.println("任务超时");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            StringBuilder ri = new StringBuilder();
            for (Integer integer : doneList) {
                ri.append(integer).append(" ");
            }
            System.out.println();
            System.out.println("本次完成的任务 ids = " + ri.toString());


        }
    }


    private static List<TaskWithId> pullFixNumTask(int num) {
        List<TaskWithId> list = Lists.newArrayList();
        while (!stack.isEmpty() && num > 0) {
            num--;
            list.add(stack.pop());
            // System.out.println("取出任务");
        }
        return list;
    }


    private static List<TaskWithId> moskListTask() {
        for (int i = 0; i < 10; i++) {
            stack.push(new TaskWithId(i));
        }
        System.out.println("所有的任务集合=" + JSON.toJSONString(stack.elements()));
        return stack;
    }


    static class TaskWithId implements Callable {
        private int id;
        private CountDownLatch latch;

        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }


        TaskWithId(int id) {
            this.id = id;
        }


        public void run() {
            try {
                Thread.sleep(new Random().nextInt(500) * id);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }

        @Override
        public Object call() throws Exception {
            run();
            System.out.println("done " + id);
            return id;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
