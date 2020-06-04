package demo;

public class PythagoreanSums {

  public static final float ZERO = 0f;
  public static void main(String[] args) {
    System.out.println(getSums(1,1,2));
  }

  /**
   * 计算两个数的平方和的根号
   * 两次迭代6.5位精度。3次迭代20位，4次迭代62位
   * @param p
   * @param q
   * @param iterCount
   * @return
   */
  public static double getSums(float p, float q, int iterCount) {

    double P = Math.abs(p);
    double Q = Math.abs(q);

    if (P < Q) {
      swap(P,Q);
    }
    if (P == ZERO){
       return P;
    }
    double R;
    for (int i = 0; i < iterCount; i++) {
      R = Q/ P;
      R = R*R;
      R = R/(4+R);
      P += 2*R* P;
      Q = Q*R;
    }
    return P;
  }

  private static void swap(double a, double b) {
    double temp = a;
    a = b;
    b = temp;
  }
}
