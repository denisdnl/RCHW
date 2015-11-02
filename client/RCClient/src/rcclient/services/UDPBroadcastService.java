/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcclient.codes.ActionCodes;
import utilities.RemoteMachineBroadcastCallback;

/**
 *
 * @author Deni-W7
 */
public class UDPBroadcastService implements Runnable{
    public static final int PORT = 50020;
    private static DatagramSocket socket;
    private static Thread thread;
    private static RemoteMachineBroadcastCallback callback;
    
    public void singletonInit(RemoteMachineBroadcastCallback callback) throws SocketException{
        
           this.callback = callback;
        if(socket == null){
            socket = new DatagramSocket(PORT);
            /*SocketAddress addr = new InetSocketAddress(PORT);
            socket.bind(addr);*/
        }
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
                byte[] data = new byte[16];
                DatagramPacket p = new DatagramPacket(data,16);
                socket.receive(p);
                String name = new String(p.getData());
                String ipAddr = p.getAddress().getHostAddress();
                callback.onFound(ipAddr + " " + name);
                
            } catch (IOException ex) {
                Logger.getLogger(UDPBroadcastService.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
    }
    
    public void stop(){
        thread.interrupt();
    }
}
