package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Socket server;
    private String method;
    private String url;
    private String httpVersion;

    public SocketThread(Socket socket) {
        server = socket;
        System.out.println("connected to remote address " + server.getRemoteSocketAddress());
        System.out.println("local address is " + server.getLocalAddress() + ":" + server.getLocalPort());
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            String str = reader.readLine();  //第一行是请求行
            String[] request = str.split(" ", 3);
            method = request[0];
            url = request[1];
            httpVersion = request[2];

            if (method.equals("GET")) {

            } else if (method.equals("POST")) {

            } else {

            }

            server.close();
            System.out.println("connection has been closed.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
