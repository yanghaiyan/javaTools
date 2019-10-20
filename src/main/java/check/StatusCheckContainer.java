package check;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 状态检查容器
 */
public class StatusCheckContainer {

  private final Map<String, IStatusCheck> scContainer = new LinkedHashMap<>();

  private static final StatusCheckContainer INSTANCE = new StatusCheckContainer();

  private StatusCheckContainer() {
    initDefault();
  }

  public static StatusCheckContainer getInstance() {
    return INSTANCE;
  }

  /**
   * 获取当前所有检查项
   */
  public Map<String, IStatusCheck> getScContainer() {
    return scContainer;
  }

  /**
   * 添加待检查项
   */
  public void addStatusCheckContainer(IStatusCheck statusCheckContainer) {
    scContainer.put(statusCheckContainer.getStatusCheckId(), statusCheckContainer);
  }

  /**
   * 获取制定名称的检查项
   */
  public IStatusCheck getStatusCheckContainer(String statusCheckId) {
    return scContainer.get(statusCheckId);
  }

  /**
   * 清除所有检查项
   */
  public void clear() {
    scContainer.clear();
  }

  /**
   * 清除制定的检查项
   */
  public IStatusCheck clearStatusCheckContainer(String statusCheckId) {
    return scContainer.remove(statusCheckId);
  }

  /**
   * 初始化默认检查项，所有共享的可在此初始化
   */
  private final void initDefault() {

  }

}

