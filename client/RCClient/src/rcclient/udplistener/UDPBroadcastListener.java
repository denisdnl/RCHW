/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.udplistener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcclient.codes.ActionCodes;
import rcclient.models.RemoteMachineBroadcastCallback;

/**
 *
 * @author Deni-W7
 */
public class UDPBroadcastListener implements Runnable{
    public static final int PORT = 50010;
    private static DatagramSocket socket;
    private static Thread thread;
    private static RemoteMachineBroadcastCallback callback;
    
    public void singletonInit(RemoteMachineBroadcastCallback callback) throws SocketException{
        
        this.callback = callback;
        if(socket == null)
            socket = new DatagramSocket();
        if(!socket.isBound())
        {   
            SocketAddress addr = new InetSocketAddress(PORT);
            socket.bind(addr);
        }
        if(thread == null){
            thread = new Thread(this);
        }
        if(thread.isInterrupted())
            thread.start();
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                byte[] data = new byte[1];
                DatagramPacket p = new DatagramPacket(data,1);
                socket.receive(p);
                if(data[0] == ActionCodes.PING){
                   String ipAddr = p.getAddress().getHostAddress();
                   callback.onFound(ipAddr);
                }
            } catch (IOException ex) {
                Logger.getLogger(UDPBroadcastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
    }
    
    public void stop(){
        thread.interrupt();
    }
}
