package src.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
//        for(String s:args) System.out.println(s);
        if (args.length >= 2 && args[0].equals("-p")) {
            try {
                port = Integer.parseInt(args[1]);
                if (port < 1024 || port > 49151) {
                    System.out.println(port + " is not a valid port number!");
                    System.out.println("port must be set in the range of [1024,49151]");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("please enter the correct port");
                System.exit(1);
            }
        }
        // 创建serverThread 实例
        Server server = new Server(port);
        server.Start();
    }
}
