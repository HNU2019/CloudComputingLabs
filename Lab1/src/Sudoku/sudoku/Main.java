package src.Sudoku.sudoku;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String path=System.getProperty("user.dir");
        path+="/Lab1/src/Sudoku/";


        path+=sc.next();
//        path+="test1";
//        path+="test1000";
//        path+="test10000";
//        System.out.println("文件路径是: "+path);

        //创建线程池
        int coresNum=Runtime.getRuntime().availableProcessors(); //获取cpu核心数
//        System.out.println("核心数："+coresNum);
        LinkedBlockingQueue<Runnable> threadQueue=new LinkedBlockingQueue();
        ThreadPoolExecutor threadPool=new ThreadPoolExecutor(coresNum,coresNum*2,
                10L, TimeUnit.MILLISECONDS,threadQueue);
        ArrayList<FutureTask> task=new ArrayList<>();  //任务队列

        /**********计时开始************/
//        long beginTime=System.nanoTime();
        //读文件
        InputStream file = new FileInputStream(path);
        BufferedReader bf = new BufferedReader(new InputStreamReader(file));
        ArrayList<char[][]> ans=new ArrayList<>();

        while(true){
            String str=bf.readLine();
            if(str==null) break;
            char[] p=str.toCharArray();
            // 检查文件是否读取成功
//            for (int i=0;i<9;i++){
//                for(int j=0;j<9;j++){
//                    System.out.print(p[i*9+j]+" ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//            task.add(new FutureTask(new BasicThread((p))));  //暴力回溯
            task.add(new FutureTask(new DancingLink(p)));  //舞蹈链

            threadPool.submit(task.get(task.size()-1));
        }

        bf.close();

        for(FutureTask f:task){
            while(true) {
                if (f.isDone() && !f.isCancelled()){
                    char[][] res={};
                    try {
                        res=(char[][]) f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
//                    for(char[] ch:res){
//                        for(char c:ch) System.out.print(c);
//                    }
//                    System.out.println(" [Thread size: "+threadPool.getPoolSize()+"]");

                    ans.add(res);
                    break;
                }else {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for(char[][] i:ans){
            for(char[] ch:i){
                for(char c:ch) System.out.print(c);
            }
            System.out.println();
        }

//        long endTime=System.nanoTime();
//        System.out.println("时间耗费为："+(endTime-beginTime)/1000+" us\n");
        threadPool.shutdown();
    }
}
