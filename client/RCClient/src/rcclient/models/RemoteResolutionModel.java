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
 * @author Deni-W7
 */
public class RemoteResolutionModel {
    public int width;
    public int height;
    
    public RemoteResolutionModel(int width,int height){
        this.width = width;
        this.height = height;
    }
    
    public RemoteResolutionModel(RawDataModel model){
        ByteBuffer wbuffer = ByteBuffer.wrap(model.data,10,4);
        ByteBuffer hbuffer = ByteBuffer.wrap(model.data,14,4);

        width = wbuffer.getInt();
        height = hbuffer.getInt();
    }
    
    public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        ByteBuffer data = ByteBuffer.allocate(1+1+8+4+4);
        data.put(ActionCodes.HEADER_START);
        data.put(ActionCodes.GET_RESOLUTION);
        data.putLong(8);//int + int
        data.putInt(width);
        data.putInt(height);
        
        result.length = 8;
        result.data = data.array();
        return result;
    }
}
