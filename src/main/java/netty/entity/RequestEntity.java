package netty.entity;

/**
 * 请求实体类
 *
 * @author YHY
 */
public class RequestEntity {

  private String content;
  private String version;
  private String extension;

  public RequestEntity(String version, String content) {
    this.content = content;
    this.version = version;
  }

  public RequestEntity() {

  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "RequestEntity{" +
        "content='" + content + '\'' +
        ", version='" + version + '\'' +
        ", extension='" + extension + '\'' +
        '}';
  }
}
