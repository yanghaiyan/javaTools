package demo.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

public class CaffeineTest {
  public static void main(String[] args) {
    Cache<String, String> cache = Caffeine.newBuilder()
        .expireAfterWrite(1, TimeUnit.SECONDS)
        .expireAfterAccess(1,TimeUnit.SECONDS)
        .maximumSize(10)
        .build();
    cache.put("hello","hello");
  }

}
