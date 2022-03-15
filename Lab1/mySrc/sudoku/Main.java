package sudoku;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String path=System.getProperty("user.dir");
        path+="/Lab1/mySrc/";

//        path+=sc.next();
        path+="test1"; //这一行以后要注释掉

        InputStream file = new FileInputStream(path);
        BufferedReader bf = new BufferedReader(new InputStreamReader(file));

        char[] problem = bf.readLine().toCharArray();
        // 检查文件是否读取成功
//        System.out.println(problem.length);
//        for (int i=0;i<9;i++){
//            for(int j=0;j<9;j++){
//                System.out.print(problem[i*9+j]+" ");
//            }
//            System.out.println();
//        }
//        System.out.println();
        bf.close();

        //创建线程池
        int coresNum=Runtime.getRuntime().availableProcessors(); //获取cpu核心数
//        System.out.println(coresNum);
        LinkedBlockingQueue<Runnable> threadQueue=new LinkedBlockingQueue(10000);
        ThreadPoolExecutor threadPool=new ThreadPoolExecutor(0,coresNum,
                10L, TimeUnit.MILLISECONDS,threadQueue);

        FutureTask task1=new FutureTask(new SudokuThread(problem));

        long beginTime=System.nanoTime();
        threadPool.submit(task1);
        char[][] res={};
        try {
            res=(char[][]) task1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for(char[] ch:res){
            for(char c:ch) System.out.print(c+" ");
            System.out.println();
        }

        long endTime=System.nanoTime();
        System.out.println("时间耗费为："+(endTime-beginTime)/1000+" us");
    }
}
