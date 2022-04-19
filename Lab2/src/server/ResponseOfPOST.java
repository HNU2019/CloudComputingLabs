package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ResponseOfPOST {
    private Socket server;
    private String url;
    private String httpVersion;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ResponseOfPOST(Socket server, String url, String httpVersion, BufferedReader reader, BufferedWriter writer) {
        this.server = server;
        this.url = url;
        this.httpVersion = httpVersion;
        this.reader = reader;
        this.writer = writer;
    }

    public void response() {
        try {
            /*
            是否出现了第二次空格
             */
            boolean twice=true;
            int times=0;
            String head;
            head= reader.readLine();
            System.out.println("::::"+head);
            while (!head.equals("")) {
                head = reader.readLine();
                System.out.println("::::"+head);
            }
            int i=0;
            while (i<13) {

                System.out.print((char) reader.read());

                i++;
            }
            System.out.println();
            // 开始处理

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
