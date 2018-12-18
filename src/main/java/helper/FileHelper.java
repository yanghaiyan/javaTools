package helper;

import exception.BasicException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileHelper {

  /**
   * 获取文件路径
   */
  public static String getFilePath(Class<?> clazz) throws BasicException {
    String filePath = clazz.getProtectionDomain().getCodeSource().getLocation()
        .getPath();
    filePath = filePath.substring(1, filePath.length());

    try {
      filePath = URLDecoder.decode(filePath, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new BasicException("url decode error", e);
    }

//    int lastIndex = filePath.lastIndexOf("/") + 1;
//    filePath = filePath.substring(0, lastIndex);

    return filePath;
  }

}
