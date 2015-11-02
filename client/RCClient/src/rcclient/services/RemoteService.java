/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcclient.codes.ActionCodes;
import rcclient.models.MouseLeftClickModel;
import rcclient.models.RawDataModel;
import rcclient.models.RemoteResolutionModel;
import rcclient.models.ScreenModel;
import rcclient.remotemachine.RemoteMachineConfig;
import utilities.RemoteResolutionCallback;
import utilities.RemoteScreenCallback;

/**
 *
 * @author Deni-W7
 */
public class RemoteService implements Runnable {
    private static Socket remoteSocket;
    private static Thread thread;
    public static boolean isStarted = false;
    public static final int PORT = 202021;
    private static SocketChannel socketChannel;
    private static RemoteScreenCallback screenCallback;
    private static RemoteResolutionCallback resCallback;
    
    public static void sendAction(RawDataModel model) {
        if(!isStarted)
            return ;
        
        if(!remoteSocket.isConnected())
            return;
      
        try {
            socketChannel.write(ByteBuffer.wrap(model.data));
        } catch (IOException ex) {
            Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void init(RemoteResolutionCallback rcallback,RemoteScreenCallback scallback) throws IOException{
        if(isStarted)
            return ;
        
        resCallback = rcallback;
        screenCallback = scallback;
        
        if(remoteSocket == null)
            remoteSocket = new Socket();
        
        if(!remoteSocket.isConnected())
        {   
            SocketAddress addr;
            addr = new InetSocketAddress(RemoteMachineConfig.IP,PORT);
            remoteSocket.connect(addr,5000);
            socketChannel = remoteSocket.getChannel();
           // socketChannel.setOption(SocketOption, addr) //blocking mode
        }
       
        if(thread == null)
            thread = new Thread(this);
   
        if(!thread.isAlive())
            thread.start();
        
        isStarted = true;
    }

    @Override
    public void run() {
        while(true){
            if(!isStarted)
                continue;
            if(!remoteSocket.isConnected())
                continue;
            
            ByteBuffer header = ByteBuffer.allocate(1);
            try {
                int rd = socketChannel.read(header);
                if(rd > 0)
                if(header.get() == ActionCodes.HEADER_START){
                    ByteBuffer actionBuff = ByteBuffer.allocate(1);
                    socketChannel.read(actionBuff);
                    ByteBuffer lengthBuff = ByteBuffer.allocate(8);
                    socketChannel.read(lengthBuff);
                    long length = lengthBuff.getLong();
                    ByteBuffer data = ByteBuffer.allocate((int) length);
                    socketChannel.read(data);
                    
                    RawDataModel model = new RawDataModel();
                    model.length = length;
                    ByteBuffer buffer = ByteBuffer.allocate((int) (1+1+8+length));
                    buffer.put(header);
                    buffer.put(actionBuff);
                    buffer.put(lengthBuff);
                    buffer.put(data);
                    model.data = buffer.array();
                    
                    byte action = actionBuff.get();
                    if(action == ActionCodes.GET_RESOLUTION){
                        RemoteResolutionModel rmodel = new RemoteResolutionModel(model);
                        resCallback.onResolutionUpdate(rmodel.width, rmodel.height);
                    }
                    
                      if(action == ActionCodes.GET_SCREEN){
                        ScreenModel smodel = new ScreenModel (model);
                        screenCallback.onScreenUpdated(smodel.bufferedBitmap);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void stop() throws IOException{
        if(remoteSocket.isConnected())
            remoteSocket.close();
        
        thread.interrupt();
        isStarted = false;
    }
}
