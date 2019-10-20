package check.constant;

public enum StatusCheckLevel {
  UP(1),
  DOWN(0);

  private int id;

  StatusCheckLevel(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
