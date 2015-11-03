/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.models;

import java.nio.ByteBuffer;
import rcclient.codes.ActionCodes;

/**
 *
 *  StartByte + ActionByte + 8Bytes:Length + 4Bytes:X + 4Bytes:Y
 */
public class MouseLocationModel {
    public int X;
    public int Y;
    
    public MouseLocationModel(int x,int y){
        this.X = x;
        this.Y = y;
    }
    
    public MouseLocationModel(RawDataModel model){
        ByteBuffer xbuff = ByteBuffer.wrap(model.data, 10, 4);
        X = xbuff.getInt();
        ByteBuffer ybuff = ByteBuffer.wrap(model.data,13,4);
        Y = ybuff.getInt();
    }
    
    public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        
        ByteBuffer data = ByteBuffer.allocate(1+1+8+4+4);
        data.put(ActionCodes.HEADER_START);
        data.put(ActionCodes.CURSOR_LOC_UPDATE);
        
        ByteBuffer lengthbuffer = ByteBuffer.allocate(8);
        lengthbuffer.putLong(8);
        data.put(lengthbuffer.array());
        
        
        ByteBuffer xbuffer = ByteBuffer.allocate(4);
        xbuffer.putInt(X);
        xbuffer.position(0);
        data.put(xbuffer.array());
        
        
        ByteBuffer ybuffer = ByteBuffer.allocate(4);
        ybuffer.putInt(Y);
        ybuffer.position(0);
        data.put(ybuffer.array());
        
        result.length = 8;//4-X + 4-Y
        result.data = data.array();
        return result;
    }
}
