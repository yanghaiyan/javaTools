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
  // ����IP�ֲ�ͬ������Ͱ, ÿ���Զ�������
  private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(1, TimeUnit.DAYS)
      .build(new CacheLoader<String, RateLimiter>() {
        @Override
        public RateLimiter load(String key) throws Exception {
          // �µ�IP��ʼ�� (����ÿ������������Ӧ)
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
