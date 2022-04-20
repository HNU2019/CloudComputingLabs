package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import static src.server.Server.*;

public class ResponseOfPOST {
    private String url;
    private String httpVersion;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File _404_html = new File("/static/404.html");

    /**
     * 构造方法中，需要把reader与writer传进来，用同一个描述符来读写
     *
     * @param url
     * @param httpVersion
     * @param reader
     * @param writer
     */
    public ResponseOfPOST(String url, String httpVersion, BufferedReader reader, BufferedWriter writer) {
        this.url = url;
        this.httpVersion = httpVersion;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * 用于返回所需的报文给客户端
     */
    public void response() {
        try {
            //body长度
            int body_length = 0;
            //body内容
            StringBuilder request_body = new StringBuilder();
            String head = reader.readLine();
            String data_type = "";
//            System.out.println(head);

            while (!head.equals("")) {
                head = reader.readLine();
//                System.out.println(head);
                if (Pattern.matches("Content-Length:.*", head)) {
                    body_length = Integer.parseInt(head.substring(16));
                }
                if (Pattern.matches("Content-Type:.*", head)) {
                    data_type = head;
                }
            }
            for (int i = 0; i < body_length; i++) {
                request_body.append((char) reader.read());
            }

//            System.out.println(request_body);

            // 开始处理
            if (url.equals("/api/echo")) {
                //处理实验二的基础要求
                return_form(request_body, body_length);
            } else if (url.equals("/api/upload")) {
                //处理实验二的高级要求
                if (data_type.equals("Content-Type: application/x-www-form-urlencoded")) {
                    return_form(request_body, body_length);
                } else {
                    return_json(request_body, body_length);
                }
            } else {
                //url错误时，返回404
                return_error();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当传输的类型为form型时，将报文处理后返回
     *
     * @param request_body
     * @param body_length
     * @throws IOException
     */
    private void return_form(StringBuilder request_body, int body_length) throws IOException {
        if (Pattern.matches(body_match_form, request_body)) {
            writer.write(httpVersion + " " + 200 + " " + "ok");
            writer.newLine();
            writer.write("Content-Type: application/x-www-form-urlencoded");
            writer.newLine();
            writer.write("Content-Length: " + body_length);
            writer.newLine();
            writer.newLine();
            writer.write(request_body.toString());
            writer.flush();
        } else {
            writer.write(httpVersion + " " + 404 + " " + "not found");
            writer.newLine();
            writer.write("Content-Type: text/plain");
            writer.newLine();
            writer.write("Content-Length: " + 40);
            writer.newLine();
            writer.newLine();
            writer.write("stauts: 403; message: Data format error;");
            writer.flush();
        }
    }

    /**
     * 当url错误时，返回404
     *
     * @throws IOException
     */
    private void return_error() throws IOException {
        writer.write(httpVersion + " " + 404 + " " + "Not Found");
        writer.newLine();
        writer.write("Content-Type: text/html");
        writer.newLine();
        writer.write("Content-Length: " + _404.length);
        writer.newLine();
        writer.newLine();
        writer.write(new String(_404, 0, _404.length));
        writer.flush();
    }

    /**
     * 当传输的类型为json型时，将报文处理后返回
     *
     * @param request_body
     * @param body_length
     * @throws IOException
     */
    private void return_json(StringBuilder request_body, int body_length) throws IOException {
        if (Pattern.matches(body_match_json, request_body)) {
            writer.write(httpVersion + " " + 200 + " " + "ok");
            writer.newLine();
            writer.write("application/json");
            writer.newLine();
            writer.write("Content-Length: " + body_length);
            writer.newLine();
            writer.newLine();
            writer.write(request_body.toString());
            writer.flush();
        } else {
            writer.write(httpVersion + " " + 404 + " " + "not found");
            writer.newLine();
            writer.write("application/json");
            writer.newLine();
            writer.write("Content-Length: " + _error_json.length);
            writer.newLine();
            writer.newLine();
            writer.write(new String(_error_json, 0, _error_json.length));
            writer.flush();
        }
    }
}
