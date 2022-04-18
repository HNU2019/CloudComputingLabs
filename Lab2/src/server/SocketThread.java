package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Socket server;

    public SocketThread(Socket socket) {
        server = socket;
        System.out.println("connected to remote address " + server.getRemoteSocketAddress());
        System.out.println("local address is " + server.getLocalAddress() + ":" + server.getLocalPort());
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            String str = reader.readLine();  //第一行是请求行，格式是"方法 URL http版本"
            String[] request = str.split(" ", 3);
            String method = request[0];

            if (method.equals("GET")) {
                ResponseOfGET doget = new ResponseOfGET(server, request[1]); // 将url传入
                doget.response();
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
