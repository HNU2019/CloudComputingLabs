package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        String serverIp = "0.0.0.0";
        int coresNum = Runtime.getRuntime().availableProcessors(); //获取cpu核心数
        int threadNum = coresNum*2;  //线程数

//        for(String s:args) System.out.println(s);
        for (int i = 0; i < args.length - 1; i += 2) {
            if (args[i].equals("-i") || args[i].equals("--ip")) {
                serverIp = args[i + 1];
            } else if (args[i].equals("-p") || args[i].equals("--port")) {
                try {
                    port = Integer.parseInt(args[i + 1]);
                    if (port < 1024 || port > 49151) {
                        System.out.println(port + " is not a valid port number!");
                        System.out.println("port must be set in the range of [1024,49151]");
                        System.exit(-1);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("please enter the correct port");
                    System.exit(-1);
                }
            } else if (args[i].equals("-t") || args[i].equals("--threads")) {
                try {
                    threadNum = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("please enter the correct number of threads");
                    System.exit(-1);
                }
            }else if(args[i].equals("--proxy")){
                i--;
                System.out.println("Sorry, we haven't accomplished the proxy server");
                System.exit(-1);
            }
        }
        //用来检查输入参数
//        System.out.println(serverIp);
//        System.out.println(port);
//        System.out.println(threadNum);

        // 创建serverThread 实例
        Server server = new Server(serverIp,port);
        server.Start();
    }
}
