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
    public static final int PORT = 50031;
    private static SocketChannel socketChannel;
    private static RemoteScreenCallback screenCallback;
    private static RemoteResolutionCallback resCallback;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static void sendAction(RawDataModel model) {
        if (!isStarted) {
            return;
        }

        if (!remoteSocket.isConnected()) {
            return;
        }

        try {
            outputStream.write(model.data);
        } catch (IOException ex) {
            Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init(RemoteResolutionCallback rcallback, RemoteScreenCallback scallback) throws IOException {
        if (isStarted) {
            return;
        }

        resCallback = rcallback;
        screenCallback = scallback;

        if (remoteSocket == null) {
            remoteSocket = new Socket();
        }

        if (!remoteSocket.isConnected()) {
            SocketAddress addr;
            addr = new InetSocketAddress(RemoteMachineConfig.IP, PORT);
            remoteSocket.connect(addr, 5000);
            inputStream = remoteSocket.getInputStream();
            outputStream = remoteSocket.getOutputStream();
            //socketChannel = remoteSocket.getChannel();
            // socketChannel.setOption(SocketOption, addr) //blocking mode
        }

        if (thread == null) {
            thread = new Thread(this);
        }

        if (!thread.isAlive()) {
            thread.start();
        }

        isStarted = true;
    }

    @Override
    public void run() {
        while (true) {
            if (!isStarted) {
                continue;
            }
            if (!remoteSocket.isConnected()) {
                continue;
            }

            byte[] header = new byte[1];
            byte[] actionBuff = new byte[1];
            try {
                int rd = inputStream.read(header, 0, 1);
                if (rd > 0) {
                    if (header[0] == ActionCodes.HEADER_START) {

                        // byte[] actionBuff = new byte[1];
                        inputStream.read(actionBuff, 0, 1);

                        if (actionBuff[0] < 1 || actionBuff[0] > 10) {
                            continue;
                        }

                        ByteBuffer lengthBuff = ByteBuffer.allocate(8);
                        byte[] lenbuf = new byte[8];
                        inputStream.read(lenbuf);
                        lengthBuff.put(lenbuf);
                        lengthBuff.position(0);
                        long length = lengthBuff.getLong();
                        byte[] data = new byte[(int) length];
                        inputStream.read(data);

                        RawDataModel model = new RawDataModel();
                        model.length = length;
                        ByteBuffer buffer = ByteBuffer.allocate((int) (1 + 1 + 8 + length));
                        buffer.put(header);
                        buffer.put(actionBuff);
                        buffer.put(lenbuf);
                        buffer.put(data);
                        model.data = buffer.array();
                        byte action = actionBuff[0];
                        if (action == ActionCodes.GET_RESOLUTION) {
                            RemoteResolutionModel rmodel = new RemoteResolutionModel(model);
                            resCallback.onResolutionUpdate(rmodel.width, rmodel.height);
                        }

                        if (action == ActionCodes.GET_SCREEN) {
                            ScreenModel smodel = new ScreenModel(model);
                            screenCallback.onScreenUpdated(smodel.bufferedBitmap);
                        }

                        buffer = null;
                        data = null;
                        header = null;
                        actionBuff = null;
                        lengthBuff = null;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(RemoteService.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }

    public void stop() throws IOException {
        remoteSocket.close();
        thread.interrupt();
        isStarted = false;
    }
}
