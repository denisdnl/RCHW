/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.udpbroadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author flash
 */
public class UDPBroadcast implements Runnable {
    public static final int PORT = 50020;
    private static DatagramSocket socket;
    private static Thread thread;
    private static String message = "";
    private static InetSocketAddress inetSocketAddress;
    
    public void singletonInit(InetAddress broadcastAddress, String message) throws SocketException, UnknownHostException{
        UDPBroadcast.message = message;
        if(socket == null) {
            socket = new DatagramSocket(PORT);
            socket.setBroadcast(true);
        }
        inetSocketAddress = new InetSocketAddress(broadcastAddress, PORT);
        
        if(thread == null){
            thread = new Thread(this);
        }
        if(!thread.isAlive())
            thread.start();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(16);
                buffer.put(this.getTruncatedMessage(message));
                
                DatagramPacket p = new DatagramPacket(buffer.array(),16, inetSocketAddress);
                socket.send(p);
            } catch (IOException ex) {
                Logger.getLogger(UDPBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
    }
    
    private byte[] getTruncatedMessage(String message) {
        byte[] data = new byte[16];
        if(message.length()>16) {
            System.arraycopy(message.getBytes(), 0, data, 0, data.length);
            return data;
        }
        
        return message.getBytes();
    }
    
    public void start(InetAddress broadcastAddress, String message) {
        try {
            try {
                this.singletonInit(broadcastAddress, message);
            } catch (UnknownHostException ex) {
                Logger.getLogger(UDPBroadcast.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPBroadcast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop(){
        thread.interrupt();
    }
    
}
