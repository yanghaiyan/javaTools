package demo.cs;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MessageUtils {

  public static void sendMessage(Socket socket, String message) throws IOException {
    OutputStream stream = socket.getOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
    objectOutputStream.writeUTF(message);
    objectOutputStream.flush();
  }

  public static String getMessage (Socket socket) throws IOException {
    InputStream inputStream = socket.getInputStream();
    ObjectInputStream ois = new ObjectInputStream(inputStream);
    return ois.readUTF();

  }

}
