package helper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 端口检测
 * @author YHY
 */
public class PortCheckerHelper {

    public static boolean checkPort(String ip, int port) {
        return checkPort(ip, port,1000);
    }

    /**
     * 端口未占用，返回true
     * 端口占用，返回fasle
     *
     * @param ip
     * @param port
     * @param timeout
     * @return
     */
    public static boolean checkPort(String ip, int port, int timeout) {

        if (port < 0) {
            return true;
        }
        String ipAdd = "127.0.0.1";
        if (StringUtils.isBlank(ip)) {
            ipAdd = ip;
        }

        boolean invalid = false;
        try {
            Socket server = new Socket();
            InetSocketAddress address = new InetSocketAddress(ipAdd, port);
            server.connect(address, timeout);
        } catch (IOException e) {
            invalid = true;
        }
        return invalid;
    }
}
