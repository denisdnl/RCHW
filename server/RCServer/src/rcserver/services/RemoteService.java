/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcserver.codes.ActionCodes;
import rcserver.models.KeyboardStrokeModel;
import rcserver.models.MouseLeftClickModel;
import rcserver.models.MouseLocationModel;
import rcserver.models.MouseRightClickModel;
import rcserver.models.MouseScrollModel;
import rcserver.models.RawDataModel;
import utilities.RemoteMachineRecieveCallback;

/**
 *
 * @author flash
 */
public class RemoteService implements Runnable {
    private static ServerSocket serverSocket;
    private static Thread thread;
    public static boolean isStarted = false;
    public static final int PORT = 353;
    private static SocketChannel socketChannel = null;
    
    private static RemoteMachineRecieveCallback recieveCallback;

    private static boolean isConnected = false;
    
    @Override
    public void run() {
        Socket newClient = null;
        while(true) {
            if(!isConnected) {
                try {
                    newClient = serverSocket.accept();
                    socketChannel = newClient.getChannel();
                    isConnected = true;
                    recieveCallback.onConnect();
                    System.out.println("CONNECTED!");
                } catch (IOException ex) {
                    Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(newClient == null || !newClient.isConnected()) {
                isConnected = false;
                recieveCallback.onDisconnect();
                System.out.println("DISCONNECTED!");
            } else {
                ByteBuffer header = ByteBuffer.allocate(1);
                try {
                    socketChannel.read(header);
                    if(header.get() == ActionCodes.HEADER_START) {
                        //yay, got data!
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
                        switch(action) {
                            case ActionCodes.CURSOR_LOC_UPDATE:
                                MouseLocationModel mouseLoc = new MouseLocationModel(model);
                                recieveCallback.onCursorLocation(mouseLoc);
                                break;
                            case ActionCodes.KEY_STROKE:
                                KeyboardStrokeModel keyStroke = new KeyboardStrokeModel(model);
                                recieveCallback.onKeyStroke(keyStroke);
                                break;
                            case ActionCodes.LEFT_CLICK:
                                MouseLeftClickModel mouseLeftClick = new MouseLeftClickModel();
                                recieveCallback.onLeftClick(mouseLeftClick);
                                break;
                            case ActionCodes.RIGHT_CLICK:
                                MouseRightClickModel mouseRightClick = new MouseRightClickModel();
                                recieveCallback.onRightClick(mouseRightClick);
                                break;
                            case ActionCodes.SCROLL:
                                MouseScrollModel mouseScroll = new MouseScrollModel(model);
                                recieveCallback.onScroll(mouseScroll);
                                break;
                        }
                        
                    }
                } catch (IOException ex) {
                    Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void init(RemoteMachineRecieveCallback tcpcallback, InetAddress listenAddress) throws IOException {
        recieveCallback = tcpcallback;
        if(serverSocket == null) {
            InetAddress bullshit;
            bullshit = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(PORT, 10, bullshit);
        }
        
        if(thread == null)
            thread = new Thread(this);
   
        if(!thread.isAlive())
            thread.start();
    }
    
    
    public void start(RemoteMachineRecieveCallback tcpcallback, InetAddress listenAddress) throws IOException {
        this.init(tcpcallback, listenAddress);
        isStarted = true;
    }
    
    public void stop() {
        isStarted = false;
    }
    
}
