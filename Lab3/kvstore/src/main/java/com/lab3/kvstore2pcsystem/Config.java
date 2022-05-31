package com.lab3.kvstore2pcsystem;


import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//全局单例config
@Component
public class Config {

    private static Config config;

    private Config() {
    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private String coIP;//协调者IP

    public String getCoIP() {
        return coIP;
    }

    public void setCoIP(String coIP) {
        this.coIP = coIP;
    }

    public int getCoPORT() {
        return coPORT;
    }

    public void setCoPORT(int coPORT) {
        this.coPORT = coPORT;
    }

    public String getParIP() {
        return parIP;
    }

    public void setParIP(String parIP) {
        this.parIP = parIP;
    }

    public int getParPORT() {
        return parPORT;
    }

    public void setParPORT(int parPORT) {
        this.parPORT = parPORT;
    }

    private int coPORT;//协调者端口
    public ArrayList<String> paIP;
    public ArrayList<String> paPORT;
    //public HashMap<String,String> pas;
    //=======================
    private String parIP;//初始化参与者时，也需要给出参与者IP和PORT
    private int parPORT;
    private int mode;       //0-协调者  1-参与者

    public void Config_intepret(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str = new String();
            while ((str = reader.readLine()) != null) {
                if (str.charAt(0) != '!') {
                    if (str.matches("^mode.+$")) {
                        if (str.indexOf("coordinator") != -1) {
                            mode = 0;
                            initCO(file);
                            return;
                        } else {
                            mode = 1;
                            initPA(file);
                            return;
                        }
                    }
                }
            }
        } else System.out.println("no such file");
    }

    public void initCO(File file) throws IOException {
        coIP = new String();
        paIP = new ArrayList<>();
        paPORT = new ArrayList<>();
        int cnt = 0;
        String str = new String();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while ((str = reader.readLine()) != null) {
            if (str.indexOf("coordinator_info") != -1) {
                String addr = str.substring(str.indexOf(' ') + 1);
                String[] split = addr.split(":");
                coIP = split[0];
                coPORT = new Integer(split[1]);
                //System.out.println(addr);
            } else if (str.indexOf("participant_info") != -1) {
                String addr = str.substring(str.indexOf(' ') + 1);
                String[] split = addr.split(":");
                paIP.add(split[0]);
                paPORT.add(split[1]);
                ++cnt;
                //System.out.println("in config "+pas.get(split[0]));
            }
        }
    }

    public void initPA(File file) throws IOException {
        parIP = new String();
        String str = new String();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while ((str = reader.readLine()) != null) {
            if (str.indexOf("coordinator_info") != -1) {
                String addr = str.substring(str.indexOf(' ') + 1);
                String[] split = addr.split(":");
                coIP = split[0];
                coPORT = new Integer(split[1]);
                // System.out.println(addr);
            } else if (str.indexOf("participant_info") != -1) {
                String addr = str.substring(str.indexOf(' ') + 1);
                String[] split = addr.split(":");
                parIP = split[0];
                parPORT = new Integer(split[1]);
                // System.out.println(addr);
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        config.Config_intepret("F:/2020云计算/George/Lab3/kvstore/src/main/resources/coordinator.conf");
    }
}
