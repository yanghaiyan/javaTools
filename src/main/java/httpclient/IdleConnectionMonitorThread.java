package httpclient;


import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;

/**
 * @description:
 */
public class IdleConnectionMonitorThread extends Thread {

  private final HttpClientConnectionManager connMgr;
  private volatile boolean exitFlag = false;

  public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
    this.connMgr = connMgr;
    setDaemon(true);
  }

  @Override
  public void run() {
    while (!this.exitFlag) {
      synchronized (this) {
        try {
          this.wait(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      // �ر�ʧЧ������
      connMgr.closeExpiredConnections();
      // ��ѡ��, �ر�30���ڲ��������
      connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
    }
  }

  public void shutdown() {
    this.exitFlag = true;
    synchronized (this) {
      notify();
    }
  }

}
