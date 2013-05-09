/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.logviewer;
import java.net.*; 
import java.io.*;
/**
 *
 * @author smitty
 */
public class LogViewer implements Runnable {
    
  public void run()
    { 
     try
     { 
      DatagramSocket serverSocket = new DatagramSocket(17654); 
  
      byte[] receiveData = new byte[1500];
  
      while(true) 
        { 

          DatagramPacket receivePacket = 
             new DatagramPacket(receiveData, receiveData.length); 

          serverSocket.receive(receivePacket); 

          String logEntry = new String(receivePacket.getData()); 
            
          System.out.println (logEntry.trim());
          
          receiveData = new byte[1500];
        } 

     }
      catch (SocketException ex) {
        System.out.println("UDP Port 17654 is occupied.");
        System.exit(1);
      }
     catch (IOException e) {
         System.out.println("IOException occurred.");
     }

    } 
}
