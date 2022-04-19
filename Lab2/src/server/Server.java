package src.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class Server {
    private final int port;
    private ServerSocket listener;
    protected final static String filePath = "/[^/\\:\\*\\?\"<>]*\\.[^/\\:\\*\\?\"<>]+"; //文件名的正则表达式
    protected final static String search = "/api/search\\?(id=(\\d+))?&?(name=(.+))?";  //search的正则表达式
    protected static Pattern searchRegex;

    public Server(String serverIp, int port) {
        this.port = port;
        try {
            listener = new ServerSocket(port, 50, InetAddress.getByName(serverIp));
//            listener = new ServerSocket(port);
            System.out.println("Local socket address is " + listener.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchRegex = Pattern.compile(search);
    }

    public void Start() {
        while (true) {
            try {
                System.out.println("port [" + port + "] is waiting for connection...");
                SocketThread connction=new SocketThread(listener.accept());
                // 已连接到客户端
                connction.run();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
