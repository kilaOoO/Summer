package com.bingsenh.Summer;

import com.bingsenh.Summer.network.BioEndPoint;

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
    }
}
