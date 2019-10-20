package pool;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import java.util.concurrent.TimeUnit;

public class CommonThreadUser extends HystrixCommand<String> {

  private  String userName;

  protected CommonThreadUser(String userName) {
    super(Setter.withGroupKey(
        //�������
        HystrixCommandGroupKey.Factory.asKey("UserGroup"))
        //�̷߳���
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserPool"))

        //�̳߳�����
        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
            .withCoreSize(10)
            .withKeepAliveTimeMinutes(5)
            .withMaxQueueSize(10)
            .withQueueSizeRejectionThreshold(10000))

        //�̳߳ظ���
        .andCommandPropertiesDefaults(
            HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(
                    HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
    );
    this.userName = userName;
  }

  @Override
  protected String run() throws Exception {
    System.out.println("userName=[" + userName + "]");
    TimeUnit.MILLISECONDS.sleep(100);
    return "userName=" + userName;
  }
}
