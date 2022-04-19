package src.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class Server {
    private final int port;
    private ServerSocket listener;
    /**
     * 文件名的正则表达式
     */
    protected final static String filePath = "/[^/\\:\\*\\?\"<>]*\\.[^/\\:\\*\\?\"<>]+";
    /**
    search的正则表达式
     */
    protected final static String search = "/api/search\\?(id=(\\d+))?&?(name=(.+))?";
    /**
     * 当类型为application/x-www-form-urlencoded时的body中的内容正则表达式
     */
    protected final  static  String body_match_form = "(id=(\\d+))?&?(name=(.+))?";
    protected final  static  String body_match_json = "\'{\"id\":\"(\\d+)\",\"name\":\"(.+)\"}\'";

    protected static Pattern searchRegex,body_match_regex;

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
        body_match_regex = Pattern.compile(body_match_form);
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
