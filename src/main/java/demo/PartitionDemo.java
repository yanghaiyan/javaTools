package demo;

import java.util.Random;

public class PartitionDemo {


  public static void main(String[] args) {
    int[] oldList = {1, 12, 14, 8, 9, 5, 35, 44, 22, 17};
    select(3, 9, oldList);
    for (int i = 0; i < 10; i++) {
      System.out.print(oldList[i] + ",");
    }
  }

  public static void select(int k, int N, int[] oldList) {
    if (k > N || k < 1) {
      return;
    }
    int l = 1, u = N;
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
