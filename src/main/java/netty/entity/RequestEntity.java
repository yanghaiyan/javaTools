package netty.entity;

/**
 * ����ʵ����
 * @author YHY
 */
public class RequestEntity {

  private String content;



  private String version;
  private String extension;

  public RequestEntity(String content, String version) {
    this.content = content;
    this.version = version;
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
