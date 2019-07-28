package demo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 多线程随机数
 */
public class RanDomUtil {

  public static void main(String[] args) {

    Runnable test = new Runnable() {
      @Override
      public void run() {
        List<Integer> rands;
        int[] rand = ThreadLocalRandom.current().ints(0, 10).distinct().limit(10).toArray();
        rands = Arrays.stream(rand).boxed().collect(Collectors.toList());
        System.out.println(rands);
      }
    };

    ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i++) {
      poolExecutor.submit(test);
    }
    poolExecutor.shutdown();
  }

}
