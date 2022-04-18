package src.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private ServerSocket listener;

    public Server(String serverIp, int port) {
        this.port = port;
        try {
            listener = new ServerSocket(port, 50, InetAddress.getByName(serverIp));
//            listener = new ServerSocket(port);
            System.out.println("Local socket address is " + listener.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
