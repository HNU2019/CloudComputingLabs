package sudoku;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String path=System.getProperty("user.dir");
        path+="/Lab1/mySrc/";

//        path+=sc.next();
        path+="test1"; //这一行测试后要注释掉

        InputStream file = new FileInputStream(path);
        BufferedReader bf = new BufferedReader(new InputStreamReader(file));

        char[] problem = bf.readLine().toCharArray();
//        // 检查文件是否读取成功
//        System.out.println(problem.length);
//        for (char c : problem) System.out.print(c + " ");
//        System.out.println();
        bf.close();

        int coresNum=Runtime.getRuntime().availableProcessors();
//        System.out.println(coresNum);
        LinkedBlockingQueue<Runnable> threadQueue=new LinkedBlockingQueue(5);
        ThreadPoolExecutor threadPool=new ThreadPoolExecutor(coresNum,2*coresNum,
                10L, TimeUnit.MILLISECONDS,threadQueue);

    }
}
