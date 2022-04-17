package src.server;

import java.io.DataInputStream;
import java.io.IOException;
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
            System.out.println("Local socket address is "+listener.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Start() {
        while (true) {
            try {
                System.out.println("port [" + port + "] is waiting for connection...");
                Socket server = listener.accept();
                System.out.println("remote address is: " + server.getRemoteSocketAddress());
                System.out.println("local address is: " + server.getLocalAddress() + ":" + server.getLocalPort());
                DataInputStream din = new DataInputStream(server.getInputStream());
                String str = din.readUTF();

                while (!str.equals("")) {
                    System.out.println(str);
                    str = din.readUTF();
                }

                server.close();
                System.out.println("connection has been closed.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
