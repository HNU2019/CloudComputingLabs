package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import static src.server.Server.body_match_json;
import static src.server.Server.body_match_form;

public class ResponseOfPOST {
    private Socket server;
    private String url;
    private String httpVersion;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File _404_html = new File("/static/404.html");
    private byte[] _404_byte = new byte[(int) _404_html.length()];


    public ResponseOfPOST(Socket server, String url, String httpVersion, BufferedReader reader, BufferedWriter writer) {
        this.server = server;
        this.url = url;
        this.httpVersion = httpVersion;
        this.reader = reader;
        this.writer = writer;
    }

    public void response() {
        try {
            //body长度
            int body_length = 0;
            //body内容
            StringBuilder request_body = new StringBuilder();
            String head = reader.readLine();
            String data_type="";
            System.out.println(head);

            while (!head.equals("")) {
                head = reader.readLine();
                System.out.println(head);
                if(Pattern.matches("Content-Length:.*",head)){
                    body_length = Integer.parseInt(head.substring(16));
                }
                if(Pattern.matches("type: .*",head)){
                    data_type = head;
                }
            }
            for (int i = 0; i < body_length; i++) {
                request_body.append((char)reader.read());
            }

            System.out.println(request_body);
            // 开始处理
            if(url.equals("/api/echo")){
                return_form(request_body,body_length);
            }else if(url.equals("/api/upload")){
                if(data_type.equals("")){

                }
            }else{

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void return_form(StringBuilder request_body,int body_length) throws IOException {
        if(Pattern.matches(body_match_form,request_body)) {
            writer.write(httpVersion + " " + 200 + " " + "ok");
            writer.newLine();
            writer.write("Content-Type: application/x-www-form-urlencoded");
            writer.newLine();
            writer.write("Content-Length: " + body_length);
            writer.newLine();
            writer.newLine();
            writer.write(request_body.toString());
            writer.newLine();
            writer.newLine();
            writer.flush();
        }else {
            writer.write(httpVersion + " " + 404 + " " + "not found");
            writer.newLine();
            writer.write("Content-Type: text/plain");
            writer.newLine();
            writer.write("Content-Length: " + 17);
            writer.newLine();
            writer.write("Data format error");
            writer.newLine();
            writer.newLine();
            writer.flush();
        }
    }
}
