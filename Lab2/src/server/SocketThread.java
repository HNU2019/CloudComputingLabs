package src.server;

import java.io.*;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Socket server;

    public SocketThread(Socket socket) {
        server = socket;
        System.out.println("connected to remote address " + server.getRemoteSocketAddress());
        System.out.println("local address is " + server.getLocalAddress() + ":" + server.getLocalPort() + '\n');
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));


            //第一行是请求行，格式是"method URL http版本"
            String str = reader.readLine();
            System.out.println("/***** request *****/\n" + str);
            String[] request = str.split("[ ]+", 3);
//            for(String s:request) System.out.printf("%s ",s);
//            System.out.println();
            String method = request[0];

            if (method.equals("GET")) {
                // 不传入reader会引发阻塞
                ResponseOfGET doGet = new ResponseOfGET(server, request[1], request[2], reader, writer);
                doGet.response();
            } else if (method.equals("POST")) {
                ResponseOfPOST doPost = new ResponseOfPOST(server, request[1], request[2], reader, writer);
                doPost.response();
            } else {

            }

            server.close();
            System.out.println("connection has been closed.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
