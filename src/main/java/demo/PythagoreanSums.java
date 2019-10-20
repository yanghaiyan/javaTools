package demo;

public class PythagoreanSums {

  public static final float ZERO = 0f;
  public static void main(String[] args) {
    System.out.println(getSums(1,1,2));
  }

  /**
   * ������������ƽ���͵ĸ���
   * ���ε���6.5λ���ȡ�3�ε���20λ��4�ε���62λ
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
