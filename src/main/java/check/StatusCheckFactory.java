package check;

import java.util.Map;

public class StatusCheckFactory {

  private static class Inner {
    static StatusCheckFactory instance = new StatusCheckFactory();
  }

  public static StatusCheckFactory getInstance() {
    return Inner.instance;
  }

  private StatusCheckFactory() {
  }

  /**
   * 检查
   */
  public void check() throws Exception {
    Map<String, IStatusCheck> StatusCheckMap = StatusCheckContainer.getInstance().getScContainer();
    for (IStatusCheck check : StatusCheckMap.values()) {
      check.statusCheck();
    }
  }

  /**
   * 对指定的状态进行检查
   */
  public void check(String... statusNameList) throws Exception {
    if (statusNameList != null) {
      for (String statusName : statusNameList) {
        Map<String, IStatusCheck> StatusCheckMap = StatusCheckContainer.getInstance()
            .getScContainer();
        IStatusCheck sc = StatusCheckMap.get(statusName);
        if (sc != null) {
          sc.statusCheck();
        }
      }
    }
  }

  /**
   * 更新检查
   */
  public void reCheck() throws Exception {
    check();
  }

}

