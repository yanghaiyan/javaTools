package algorithm.random;

import java.util.Random;

/*
 *  选取第K小值
 * */
public class PartitionDemo {

  public static void main(String[] args) {
    int[] oldList = {7, 10, 5, 56, 4, 55, 99, 100, 13, 79, 9, 1, 35, 78, 54, 173};
    sortK(4, 10, oldList);
    //System.out.println(select(4, 15, oldList));
    for (int i = 0; i < oldList.length; i++) {
      System.out.print(oldList[i] + ",");
    }
  }

  /**
   * 在前N个节点中把小于第小k的节点都移到前面，大于该值放在后面
   *
   * @param k 选定节点
   */
  public static void sortK(int k, int N, int[] oldList) {
    if (k > N || k < 1) {
      return;
    }
    int l = 0, u = N;
    Random random = new Random();
    int m = l;
    while (l < u) {
      m = partitionList(oldList, l, u, random);
      if (k <= m) {
        u = m - 1;
      } else {
        l = m + 1;
      }
    }
  }

  /**
   * 前N个中第K小值
   */
  public static int selectk(int k, int N, int[] list) {
    if (k > N || k < 1) {
      System.out.println("error");
    }
    int l = 0, u = N;
    Random random = new Random();
    int m = l;
    while (l < u) {
      m = partitionList(list, l, u, random);
      if (k == m) {
        return list[m];
      } else if (k < m) {
        u = m - 1;
      } else {
        l = m + 1;
      }
    }

    return list[m];
  }

  private static int partitionList(int[] oldList, int l, int u, Random rand) {
    swap(l, l + rand.nextInt(u), oldList);
    int m = l;
    for (int i = l + 1; i <= u; i++) {
      if (oldList[i] < oldList[l]) {
        m++;
        swap(m, i, oldList);
      }
    }

    swap(l, m, oldList);
    return m;
  }

  private static void swap(int a, int b, int[] array) {
    int temp = array[a];
    array[a] = array[b];
    array[b] = temp;
  }
}
