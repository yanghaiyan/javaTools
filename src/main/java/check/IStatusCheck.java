package check;

import check.constant.StatusCheckLevel;
import java.util.Map;

public interface IStatusCheck {

  /**
   * 获取状态检查任务标识
   */
  String getStatusCheckId();

  /**
   * 获取状态检查任务名称（标识）
   */
  String getStatusCheckName();

  /**
   * 执行状态检查
   */
  void statusCheck() throws Exception;

  /**
   * 获取当前检查结果级别
   */
  StatusCheckLevel getStatusCheckLevel();

  /**
   * 获取检查状态结果（标识）
   */
  Map<String, Object> getStatusCheckValue();

  /**
   * 获取检查状态结果（名称）
   */
  Map<String, Object> getStatusCheckValueName();

  /**
   * 获取检查状态的样式
   */
  Map<String, Object> getStatusCheckStyle();
}
