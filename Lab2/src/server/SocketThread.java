package src.server;

import java.io.*;
import java.net.Socket;

public class SocketThread implements Runnable {
    private Socket server;
    private BufferedReader reader;
    private BufferedWriter writer;

    public SocketThread(Socket socket) {
        server = socket;
        System.out.println("connected to remote address " + server.getRemoteSocketAddress());
        System.out.println("local address is " + server.getLocalAddress() + ":" + server.getLocalPort() + '\n');
        try {
            reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件并作为body发送
     * @param writer
     * @param file
     */
    protected static void sendFileInBody(BufferedWriter writer, File file) {
        try {
            long fileLength = file.length();
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));

            byte[] bytes = new byte[8192];
            int length = 0;
            while (length < fileLength) {
                length += fin.read(bytes, 0, bytes.length);
                writer.write(new String(bytes,0,(length-1)%bytes.length+1));
            }
            fin.close();  //关闭文件输入流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

            String str = reader.readLine();  //第一行是请求行，格式是"method URL http版本"
            System.out.println("/***** request *****/\n" + str);
            String[] request = str.split("[ ]+", 3);
//            for(String s:request) System.out.printf("%s ",s);
//            System.out.println();
            String method = request[0];

            if (method.equals("GET")) {
                ResponseOfGET doGet = new ResponseOfGET(server, request[1], request[2], reader, writer); // 不传入reader会引发阻塞
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
