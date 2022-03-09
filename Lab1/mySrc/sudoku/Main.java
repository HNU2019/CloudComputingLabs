package sudoku;

import java.io.*;
import java.util.Scanner;

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
        System.out.println(problem.length);
        for (char c : problem) System.out.print(c + " ");
        System.out.println();
    }
}
