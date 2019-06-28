package com.bingsenh.Summer;

import com.bingsenh.Summer.nio.HttpServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 服务器启动入口
 */
public class BootStrap
{
    public static void main( String[] args )
    {
//        BioEndPoint server = new BioEndPoint();
//        server.start(1234);
//        HttpServer httpServer = null;
//        try {
//            httpServer = new HttpServer(1234);
//            httpServer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Scanner scanner = new Scanner(System.in);
//        String order;
//        while(scanner.hasNext()){
//            order = scanner.next();
//            if(order.equals("exit")){
//                System.exit(0);
//                httpServer.closeServer();
//            }
//        }
        Map<String,String> strs = new HashMap<>();
        String s = strs.get("a");
        if(s == null){
            System.out.println("null");
        }else {
            System.out.println("not null");
        }
    }
}
