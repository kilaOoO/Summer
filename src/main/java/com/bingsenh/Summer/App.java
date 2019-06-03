package com.bingsenh.Summer;

import com.bingsenh.Summer.connector.context.ServletContext;
import com.bingsenh.Summer.network.BioEndPoint;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BioEndPoint server = new BioEndPoint();
        server.start(1234);
        Scanner scanner = new Scanner(System.in);
        String order;
        while(scanner.hasNext()){
            order = scanner.next();
            if(order.equals("exit")){
                server.close();
                System.exit(0);
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
