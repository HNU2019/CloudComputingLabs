package src.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Server {
    private final String ip;
    private final int port;
    private ServerSocket listener;
    protected final static String filePath = "/([^\\:\\*\\?\"<>]*)\\.([^/\\:\\*\\?\"<>]+)"; //文件名的正则表达式
    protected final static String search = "/api/search\\?(id=(\\d+))?&?(name=(.+))?";  //search的正则表达式
    /**
     * 当类型为application/x-www-form-urlencoded时的body中的内容正则表达式
     */
    protected final static String body_match_form = "(id=(\\d+))?&?(name=(.+))?";
    protected final static String body_match_json = "\\{\"id\":\\d+,\"name\":\".+\"\\}";
    protected static Pattern searchRegex;
    protected static Pattern fileRegex;
    // 预先读入状态码文件
    protected static byte[] _403;
    protected static byte[] _404;
    protected static byte[] _501;
    protected static byte[] _502;
    protected static byte[] data_txt; // data.txt
    protected static byte[] data_json; // data.json
    /**
     * 当传输类型为json时，发生错误的需要返回的字节，将字节数组读入BufferedReader中
     */
    protected static byte[] _error_json;

    public Server(String serverIp, int port) {
        this.ip=serverIp;
        this.port = port;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            listener = new ServerSocket();
            listener.setReuseAddress(true);
            SocketAddress socketAddress = new InetSocketAddress(serverIp, port);
            listener.bind(socketAddress, 50);
//            System.out.println("Local socket address is " + listener.getLocalSocketAddress());
        } catch (IOException e) {

        }
        searchRegex = Pattern.compile(search);  // 静态编译search正则表达式
        fileRegex = Pattern.compile(filePath);
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
        File error_json_file = new File("data/error.json");
        _403 = new byte[(int) file403.length()];
        _404 = new byte[(int) file404.length()];
        _501 = new byte[(int) file501.length()];
        _502 = new byte[(int) file502.length()];
        data_txt = new byte[(int) dataTxt.length()];
        data_json = new byte[(int) dataJson.length()];
        _error_json = new byte[(int) error_json_file.length()];

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
            //读 error.json
            fin = new BufferedInputStream(new FileInputStream(error_json_file));
            fin.read(_error_json, 0, _error_json.length);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Start() {
        // 创建线程池
        int coresNum=Runtime.getRuntime().availableProcessors(); //获取cpu核心数
        LinkedBlockingQueue<Runnable> workQueue=new LinkedBlockingQueue<>(50);
        ThreadPoolExecutor pool=new ThreadPoolExecutor(coresNum*2-1,coresNum*10-1,
                10L, TimeUnit.MILLISECONDS,workQueue);

        while (true) {
            try {
//                System.out.println("port [" + port + "] is waiting for connection...");
                SocketThread connction = new SocketThread(listener.accept());
                // 已连接到客户端
                pool.submit(connction);
            }catch (BindException e){
                try {
                    SocketAddress socketAddress = new InetSocketAddress(ip, port);
                    listener.bind(socketAddress, 50);
//            System.out.println("Local socket address is " + listener.getLocalSocketAddress());
                } catch (IOException e2) {

                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
