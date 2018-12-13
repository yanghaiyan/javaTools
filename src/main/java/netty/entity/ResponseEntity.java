package netty.entity;

/**
 * 响应实体类
 */
public class ResponseEntity {

  private String version;
  private String content;
  private String error;
  private String success;
  private String extension;

  public ResponseEntity(String version, String content) {
    this.version = version;
    this.content = content;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getSuccess() {
    return success;
  }

  public void setSuccess(String success) {
    this.success = success;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  @Override
  public String toString() {
    return "ResponseEntity{" +
        "version='" + version + '\'' +
        ", content='" + content + '\'' +
        ", error='" + error + '\'' +
        ", success='" + success + '\'' +
        ", extension='" + extension + '\'' +
        '}';
  }



}
