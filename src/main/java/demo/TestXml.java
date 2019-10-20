package demo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class TestXml {

  public static void main(String[] args) {
    XStream xstream = new XStream(new StaxDriver());
    XStream.setupDefaultSecurity(xstream);
  }
}
