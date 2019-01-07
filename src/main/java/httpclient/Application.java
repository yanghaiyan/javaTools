package httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import netty.entity.RequestEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * 测试案例
 */
public class Application {


  static String getUrl = "https://www.baidu.com";

  static String postUrl = "https://10.0.90.45:8099/post";

  private static String ipUrlStr = "http://ip.taobao.com/service/getIpInfo.php?ip=";

  private static String myIp = "101.231.201.50";

  @Test
  public void testGet() {
    PoolHttpClient httpClient = new PoolHttpClient();
    String resp = httpClient.doGet(getUrl);
    ;
    Document document = Jsoup.parse(resp);
    Elements links = document.getElementsByClass("content__list--item");
    for (Element element : links) {
      System.out.println(element.toString());
      ;
    }
  }

  @Test
  public void testPost() {
    PoolHttpClient httpClient = new PoolHttpClient();
    RequestEntity request = new RequestEntity("1.0", "hello");
    String str = JSON.toJSONString(request);
    for (int i = 0; i < 1; i++) {
      String resp = httpClient.doPost(postUrl, str);
      System.out.println(resp);
    }
  }

  @Test
  public void getIpAddr() {
    PoolHttpClient httpClient = new PoolHttpClient();
    String url = ipUrlStr + myIp;
    String result = httpClient.doGet(url);
    JSONObject jsonObject = JSON.parseObject(result);
    System.out.println("国家： " + jsonObject.getJSONObject("data").get("country"));
    System.out.println("省份： " + jsonObject.getJSONObject("data").get("region"));
    System.out.println("城市：" + jsonObject.getJSONObject("data").get("city"));

  }
}
