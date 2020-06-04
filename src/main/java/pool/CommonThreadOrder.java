package pool;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import java.util.concurrent.TimeUnit;

public class CommonThreadOrder extends HystrixCommand<String> {


  private String orderName;

  public CommonThreadOrder(String orderName) {
    super(Setter.withGroupKey(
        //服务分组
        HystrixCommandGroupKey.Factory.asKey("OrderGroup"))
        //线程分组
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("OrderPool"))

        //线程池配置
        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
            .withCoreSize(10)
            .withKeepAliveTimeMinutes(5)
            .withMaxQueueSize(10)
            .withQueueSizeRejectionThreshold(10000))

        .andCommandPropertiesDefaults(
            HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(
                    HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
    );
 this.orderName = orderName;
  }

  @Override
  protected String run() throws Exception {
    System.out.println("orderName=[" + orderName + "]");
    TimeUnit.MILLISECONDS.sleep(100);
    return "OrderName=" + orderName;
  }
}
