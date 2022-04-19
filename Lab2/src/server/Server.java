package src.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.regex.Pattern;

public class Server {
    private final int port;
    private ServerSocket listener;
    protected final static String filePath = "/([^/\\:\\*\\?\"<>]*)\\.([^/\\:\\*\\?\"<>]+)"; //文件名的正则表达式
    protected final static String search = "/api/search\\?(id=(\\d+))?&?(name=(.+))?";  //search的正则表达式
    protected static Pattern searchRegex;
    // 预先读入状态码文件
    protected static byte[] _403;
    protected static byte[] _404;
    protected static byte[] _501;
    protected static byte[] _502;
    protected static byte[] data_txt; // data.txt
    protected static byte[] data_json; // data.json

    public Server(String serverIp, int port) {
        this.port = port;
        try {
            listener = new ServerSocket(port, 50, InetAddress.getByName(serverIp));
//            listener = new ServerSocket(port);
            System.out.println("Local socket address is " + listener.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchRegex = Pattern.compile(search);  // 静态编译search正则表达式

        preReadFile();
    }

    /**
     * 服务器启动时将部分文件先读入到内存，403、404、501、502、data.txt
     */
    private void preReadFile() {
        File file403 = new File("static/403.html");
        File file404 = new File("static/404.html");
        File file501 = new File("static/501.html");
        File file502 = new File("static/502.html");
        File dataTxt = new File("data/data.txt");
        File dataJson = new File("data/data.json");
        _403 = new byte[(int) file403.length()];
        _404 = new byte[(int) file404.length()];
        _501 = new byte[(int) file501.length()];
        _502 = new byte[(int) file502.length()];
        data_txt = new byte[(int) dataTxt.length()];
        data_json = new byte[(int) dataJson.length()];

        try {
            // 读403
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file403));
            fin.read(_403, 0, _403.length);
            fin.close();
            // 读404
            fin = new BufferedInputStream(new FileInputStream(file404));
            fin.read(_404, 0, _404.length);
            fin.close();
            // 读501
            fin = new BufferedInputStream(new FileInputStream(file501));
            fin.read(_501, 0, _501.length);
            fin.close();
            // 读502
            fin = new BufferedInputStream(new FileInputStream(file502));
            fin.read(_502, 0, _502.length);
            fin.close();
            // 读data.txt
            fin = new BufferedInputStream(new FileInputStream(dataTxt));
            fin.read(data_txt, 0, data_txt.length);
            fin.close();
            // 读data.json
            fin = new BufferedInputStream(new FileInputStream(dataJson));
            fin.read(data_json, 0, data_json.length);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Start() {
        while (true) {
            try {
                System.out.println("port [" + port + "] is waiting for connection...");
                SocketThread connction = new SocketThread(listener.accept());
                // 已连接到客户端
                connction.run();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
