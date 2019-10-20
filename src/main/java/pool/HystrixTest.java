package pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HystrixTest {

  public static void main(String[] args)
      throws InterruptedException, ExecutionException, TimeoutException {
    CommonThreadOrder commandPhone = new CommonThreadOrder("1");
    CommonThreadOrder command = new CommonThreadOrder("2");

    //������ʽִ��
    String execute = commandPhone.execute();
    System.out.println("execute=[" + execute + "]");

    //�첽��������ʽ
    Future<String> queue = command.queue();
    String value = queue.get(200, TimeUnit.MILLISECONDS);
    System.out.println("value=[" + value + "]");

    CommonThreadUser commandUser = new CommonThreadUser("test");
    String name = commandUser.execute();
    System.out.println("name=[" + name + "]");
  }
}
