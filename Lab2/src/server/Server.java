package src.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private ServerSocket listener;

    public Server(int port) {
        this.port = port;
        try {
            listener = new ServerSocket(port);
            listener.setSoTimeout(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Start() {
        while (true) {
            try{
                System.out.println("port ["+port+"] is waiting for connection...");
                Socket socket=listener.accept();
                System.out.println(socket.getRemoteSocketAddress());
                System.out.println(socket.getInetAddress());
                System.out.println("remote port is: "+socket.getPort());
                System.out.println("local port is: "+socket.getLocalPort());
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
