package demo.cs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/**
 *
 */
public class Server implements Runnable {

  ServerSocket serverSocket;
  volatile boolean keepProcessing = true;

  public Server(int port, int millisecondTimeout) throws IOException {
    serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(millisecondTimeout);
  }

  @Override
  public void run() {
    System.out.println("Server Run Start");

    while (keepProcessing) {
      try {
        System.out.println("accepting client\n");
        Socket socket = serverSocket.accept();
        System.out.println("got client");
        process(socket);
      } catch (IOException e) {
        handle(e);
      }
    }
  }

  private void handle(IOException e) {
    if (!(e instanceof SocketException)) {
      e.printStackTrace();
    }
  }

  private void process(Socket socket) {
    if (socket == null) {
      return;
    }

    try {
      System.out.println("Server: getting message\n");
      String message = MessageUtils.getMessage(socket);
      System.out.println("Server: gotMessage:" + message);
      Thread.sleep(1000);
      System.out.println("Server: send replay" + message);
      MessageUtils.sendMessage(socket, "Process:" + message);
      System.out.println("Server Send \n");
      closeIgnoringException(socket);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void closeIgnoringException(Socket socket) {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
      }
    }
  }

  private void closeIgnoringException(ServerSocket socket) {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
      }
    }
  }

  public void stopProcessing() {
    keepProcessing = false;
    closeIgnoringException(serverSocket);
  }
}
