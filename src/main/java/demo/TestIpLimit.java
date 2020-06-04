package demo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestIpLimit {
  // 根据IP分不同的令牌桶, 每天自动清理缓存
  private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build(new CacheLoader<String, RateLimiter>() {
        @Override
        public RateLimiter load(String key) throws Exception {
          // 新的IP初始化 (限流每秒两个令牌响应)
          return RateLimiter.create(0.1);
        }
      });

  public static void main(String[] args) throws InterruptedException, ExecutionException {
//    login("127.0.0.1");
//    Thread.sleep(200);
//    login("127.0.0.1");
//    login("127.0.0.1");
//    Thread.sleep(800);
    for (int i = 0; i <5 ; i++) {
    login("127.0.0.1");
    Thread.sleep(1000);
   // login("127.0.1.1");
    }

  }

  private static void login(String  ip) throws ExecutionException {
    RateLimiter limiter = caches.get(ip);

    if (limiter.tryAcquire()) {
      System.out.println(ip + " success " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
    } else {
      System.out.println(ip + " failed " + new SimpleDateFormat("HH:mm:ss.sss").format(new Date()));
    }
  }
}
