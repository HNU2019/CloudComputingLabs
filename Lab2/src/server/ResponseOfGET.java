package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
    private void apiCheck(){

    }

    /**
     * 发送/data/data.json文件内容
     */
    private void apiList(){

    }

    /**
     * 发送/data/data.json中匹配的所有条目
     */
    private void apiSearch(String id,String name) {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion);  //http版本
        //判断条目是否存在
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
