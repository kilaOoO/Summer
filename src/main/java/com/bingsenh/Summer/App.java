package com.bingsenh.Summer;

import com.bingsenh.Summer.nio.HttpServer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        BioEndPoint server = new BioEndPoint();
//        server.start(1234);
        HttpServer httpServer = null;
        try {
            httpServer = new HttpServer(1234);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        String order;
        while(scanner.hasNext()){
            order = scanner.next();
            if(order.equals("exit")){
                System.exit(0);
                httpServer.closeServer();
            }
        }
//
//        System.out.println(App.class.getResource(""));
//        System.out.println(App.class.getResource("/"));

//        SAXReader reader = new SAXReader();
//        Document doc = null;
//
//        try {
//            doc = reader.read(App.class.getResourceAsStream("/web.xml"));
//        } catch (DocumentException e) {
//            System.out.println("加载失败");
//            e.printStackTrace();
//        }
    }
}
