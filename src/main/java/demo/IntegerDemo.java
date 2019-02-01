package demo;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yhy
 */
public class IntegerDemo {

  /**
   * 交换两个integer类型中的值
   */
  public static void swap(Integer num1, Integer num2) {

    try {
      Field field = Integer.class.getDeclaredField("value");
      field.setAccessible(true);
      int tmp = num1.intValue();
      field.set(num1, num2);
      field.setInt(num2, tmp);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {

    ReentrantLock lock = new ReentrantLock();
    lock.lock();
    Integer a = 1;
    Integer b = 2;
    swap(a, b);
    System.out.println("a:" + a + ",b:" + b);
  }
}
