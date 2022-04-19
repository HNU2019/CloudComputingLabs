package src.server;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static src.server.Server.*;
import static src.server.SocketThread.sendFileInBody;

public class ResponseOfGET {
    private String url;
    private String httpVersion;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ResponseOfGET(String url, String version, BufferedReader bf, BufferedWriter bw) {
        this.url = url;
        httpVersion = version;
        reader = bf;
        writer = bw;
    }

    /**
     * 发送给定文件的所有内容
     * 会根据文件格式不同发送不同状态码
     *
     * @param filename 文件名
     */
    private void sendFileContent(String filename) {
        File file = new File("static/" + filename);
        //判断文件是否存在
        if (!file.exists()) {
            sendNotFound();
            return;
        }

        StringBuilder response = new StringBuilder();
        response.append(httpVersion);  //http版本
        String type = "";

        Pattern fileRegex = Pattern.compile(filePath);
        Matcher m = fileRegex.matcher(url);
        if (m.find()) {
            String left = m.group(1);   // 文件名
            String right = m.group(2);  // 文件格式
//            System.out.println(left);
//            System.out.println(right);
            if (left.equals("403")) {
                response.append(" 403 Forbidden\r\n");
            } else if (left.equals("404")) {
                response.append(" 404 Not Found\r\n");
            } else if (left.equals("501")) {
                response.append(" 501 Not Implemented\r\n");
            } else if (left.equals("502")) {
                response.append(" 502 Bad Gateway\r\n");
            } else {
                response.append(" 200 OK\r\n"); // 一般文件
            }

            if (right.equals("html")) type = "text/html";
            else if (right.equals("js")) type = "text/javascript";
            else if (right.equals("css")) type = "text/css";
            else if (right.equals("json")) type = "application/json";
        }

        try {
            writer.write(response.toString());
            long fileLength = file.length();
            writer.write(String.format("Content-type: %s\r\nContent-Length: %d\r\n\r\n", type, fileLength));
            sendFileInBody(writer, file);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送/data/data.txt文件内容
     */
    private void apiCheck() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion + " 200 OK\r\n");

        try {
            writer.write(response.toString());
            writer.write(String.format("Content-type: text/plain\r\nContent-Length: %d\r\n\r\n", data_txt.length));
            writer.write(new String(data_txt, 0, data_txt.length));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送/data/data.json文件内容
     */
    private void apiList() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion + " 200 OK\r\n");

        try {
            writer.write(response.toString());
            writer.write(String.format("Content-type: text/json\r\nContent-Length: %d\r\n\r\n", data_json.length));
            writer.write(new String(data_json, 0, data_json.length));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送/data/data.json中匹配的所有条目
     */
    private void apiSearch(String id, String name) {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion);  //http版本
        //判断条目是否存在
        String data = new String(data_json);
        StringBuilder ans = new StringBuilder("[]");
//        System.out.println(data);
        for (int i = 1; i < data.length() - 1; ) {
            // 要保证每个循环开始时，i指向'{'
            String _id = "", _name = "";
            char c;
            i += 6;  //id值的第一个字符
            c = data.charAt(i);
            while (c != ',') {
                _id += c;
                c = data.charAt(++i);
            }
            i += 9;  //指向name值的第一个字符
            c = data.charAt(i);
            while (c != '\"') {
                _name += c;
                c = data.charAt(++i);
            }
            i += 3;
            // 开始比较
//            System.out.printf("%s %s\n",_id,_name);
            if ((id == null || _id.equals(id)) && (name == null || _name.equals(name))) {
                ans.insert(ans.length()-1,String.format("{\"id\":%s,\"name\":\"%s\"}",_id,_name));
            }
        }
        if (ans.length() == 2) {  // 没有匹配的
            response.append(" 404 Not Found\r\n");
            try {
                writer.write(response.toString());
                writer.write(String.format("Content-Type: text/html\r\nContent-length: %d\r\n\r\n", _404.length));
                writer.write(new String(_404, 0, _404.length));   //读取_404数组并发送
                writer.flush();  //最后刷新缓冲区
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.append(" 200 OK\r\n");
            try{
                writer.write(response.toString());
                writer.write(String.format("Content-Type: application/json\r\nContent-length: %d\r\n\r\n", ans.length()));
                writer.write(ans.toString());
                writer.flush();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * url不正确, 发送404.html
     */
    private void sendNotFound() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion + " 404 Not Found\r\n");  //http版本

        try {
            writer.write(response.toString());  //输出响应行
            int fileLength = _404.length;
            writer.write(String.format("Content-Type: text/html\r\nContent-length: %d\r\n\r\n", fileLength));
            writer.write(new String(_404, 0, _404.length));   //读取_404数组并发送
            writer.flush();  //最后刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void response() {
        try {
            String head = reader.readLine();
            while (!head.equals("")) {
//                String[] temp = head.split(":[ ]*", 2);
//                System.out.println(temp[0] + ":" + temp[1]);
                System.out.println(head);
                head = reader.readLine();
            }
            // 开始处理
            if (url.charAt(url.length() - 1) == '/') { //请求的是目录
                sendNotFound();
            } else if (url.equals("/api/check")) {
                apiCheck();
            } else if (url.equals("/api/list")) {
                apiList();
            } else if (Pattern.matches(filePath, url)) {   //文件
                sendFileContent(url);
            } else if (Pattern.matches(search, url)) {  // search
                Matcher m = searchRegex.matcher(url);
                if (m.find()) {
                    String id = m.group(2);    //为空则为null
                    String name = m.group(4);  //为空则为null
//                    System.out.println("id = " + id);
//                    System.out.println("name = " + name);
                    apiSearch(id, name);
                }
            } else {
                sendNotFound();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
