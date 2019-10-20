package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 多线程随机数
 */
public class RanDomUtil {

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 20; i++) {
      System.out.println(random_select(100));
    }

  }

  public static void testRandom() {
    Runnable test = new Runnable() {
      @Override
      public void run() {
//        UUID uuid = new UUID(ThreadLocalRandom.current().nextLong(),
//            ThreadLocalRandom.current().nextLong());
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

  public static List<Integer> getRandoms(int num, int limit) {
    List<Integer> result = new ArrayList<Integer>(num);
    Random r = new Random();
    int j = limit - num + 1;
    for (int i = j; i <= limit; i++) {
      int random = 1 + r.nextInt(i);
      if (!result.contains(random)) {
        result.add(random);
      } else {
        result.add(i);
      }
    }
    return result;
  }

  public static List<Integer> getRandomsList(int num, int limit) {
    List<Integer> result = new ArrayList<Integer>(num);
    Random r = new Random();
    int j = limit - num + 1;
    for (int i = j; i <= limit; i++) {
      int random = 1 + r.nextInt(i);
      if (!result.contains(random)) {
        result.add(random);
      } else {
        result.add(i);
      }
    }
    return result;
  }

  /**
   * 生成1——imit的随机M元子序列
   * @param num
   * @param limit
   * @return
   */
  public static int[] getRandListByLimit(int num, int limit) {
    int resultList[] = new int[num];
    Random r = new Random();
    int bound = combination(limit, num) - 1;
    getRandoms(num, limit, r.nextInt(bound), resultList);
    return resultList;
  }

  /**
   *n个元素中m个元素的第G种组合放在数组a中
   * @param num
   * @param limit
   * @param g
   * @param result
   */
  private static void getRandoms(int num, int limit, long g, int[] result) {
    int d = 1;
    while (num > 0) {
      long t = combination(limit - d, num - 1);
      if (g - t < 0) {
        num--;
        result[num] = d;
      } else {
        g = g - t;
      }
      d++;
    }
  }

  /**
   * 以一个元素作为缓冲，随机取某个元素
   * @param n
   * @return
   */
  public static int random_select(int n) {
    int i, num = 1;
    Random rand = new Random();
    for (i = 1; i < n; i++) {
      if (rand.nextInt() % i == 0) {     //等于0或者任何一个0~i-1之间的数，表示概率1/i。
        num = i;
      }
    }//以1/i的概率替换num的缓存值。
    return num;
  }

  private static long factorial(int n) {
    return (n > 1) ? n * factorial(n - 1) : 1;
  }

  /**
   * 计算排列数，即A(n, m) = n!/(n-m)!
   */
  public static long arrangement(int n, int m) {
    return (n >= m) ? factorial(n) / factorial(n - m) : 0;
  }

  /**
   * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
   */
  public static int combination(int n, int m) {
    return (n >= m) ? (int) (factorial(n) / factorial(n - m) / factorial(m)) : 1;
  }


  public static void checknk(int n, int k) {
    if (k < 0 || k > n) { // N must be a positive integer.
      throw new IllegalArgumentException("K must be an integer between 0 and N.");
    }
  }

  /**
   * 计算排列组合
   * @param n
   * @param k
   * @return
   */
  public static int combination1(int n, int k) {
    if (n > 70 || (n == 70 && k > 25 && k < 45)) {
      throw new IllegalArgumentException(
          "N(" + n + ") and k(" + k + ") don't meet the requirements.");
    }
    checknk(n, k);
    k = (k > (n - k)) ? n - k : k;
    if (k <= 1) {  // C(n, 0) = 1, C(n, 1) = n
      return k == 0 ? 1 : n;
    }
    int cacheLen = k + 1;
    int[] befores = new int[cacheLen];
    befores[0] = 1;
    int[] afters = new int[cacheLen];
    afters[0] = 1;
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= k; j++) {
        afters[j] = befores[j - 1] + befores[j];
      }
      System.arraycopy(afters, 1, befores, 1, k);
    }
    return befores[k];
  }

}
