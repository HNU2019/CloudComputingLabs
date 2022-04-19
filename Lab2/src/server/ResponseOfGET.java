package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseOfGET {
    private Socket server;
    private String url;
    private String httpVersion;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ResponseOfGET(Socket socket, String url, String version, BufferedReader bf, BufferedWriter bw) {
        server = socket;
        this.url = url;
        httpVersion = version;
        reader = bf;
        writer = bw;
    }

    /**
     * 发送给定文件的所有内容
     *
     * @param filename 文件名
     */
    private void sendFileContent(String filename) {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion);  //http版本
        //判断文件是否存在


        try {
            writer.write(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送/data/data.txt文件内容
     */
    private void apiCheck() {

    }

    /**
     * 发送/data/data.json文件内容
     */
    private void apiList() {

    }

    /**
     * 发送/data/data.json中匹配的所有条目
     */
    private void apiSearch(String id, String name) {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion);  //http版本
        //判断条目是否存在
    }

    /**
     * url不正确, 发送404.html
     */
    private void sendNotFound() {

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
            String filePath = "/[^/\\:\\*\\?\"<>]*\\.[^/\\:\\*\\?\"<>]+";  //文件名的正则表达式
            String search = "/api/search\\?(id=(\\d+))?&?(name=(.+))?";

            if (url.charAt(url.length() - 1) == '/') { //请求的是目录
                sendNotFound();
            } else if (url.equals("/api/check")) {
                apiCheck();
            } else if (url.equals("/api/list")) {
                apiList();
            } else if (Pattern.matches(filePath, url)) {   //文件
                System.out.println(url);
            } else if (Pattern.matches(search, url)) {  // search
                Pattern regex = Pattern.compile(search);
                Matcher m = regex.matcher(url);
                if (m.find()) {
                    String id=m.group(2);    //为空则为null
                    String name=m.group(4);  //为空则为null
//                    System.out.println("id = " + id);
//                    System.out.println("name = " + name);
                }
            } else {
                sendNotFound();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
