/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.models;

import java.nio.ByteBuffer;
import rcserver.codes.ActionCodes;

/**
 *
 * @author Deni-W7
 */
public class MouseScrollModel {
    public static final byte SCROLL_UP = 1;
    public static final byte SCROLL_DOWN = 2;
    public static final byte SCROLL_PRESS = 3;
    
    public byte action;
    public MouseScrollModel(RawDataModel model){
        action = model.data[10];
    }
    
    public MouseScrollModel(byte action){
        this.action = action;
    }
    
    public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        
        ByteBuffer data = ByteBuffer.allocate(1+1+8+1);
        data.put(ActionCodes.HEADER_START);
        data.put(ActionCodes.SCROLL);
        data.putLong(1);
        data.put(action);
        
        result.length = 1;
        result.data = data.array();
        return result;
    }
}
