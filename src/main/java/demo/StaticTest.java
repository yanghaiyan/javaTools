package demo;

public class StaticTest {

  public static void main(String[] args) {
    GT<Integer> gti = new GT<Integer>();
    gti.var = 1;
    GT<String> gts = new GT<String>();
    gts.var = 2;
    System.out.println(gti.var);
  }
}

class GT<T> {

  public static int var = 0;

  public void nothing(T x) {
  }
}