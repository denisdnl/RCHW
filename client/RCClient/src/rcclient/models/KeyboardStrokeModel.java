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
 * StartByte + ActionByte + 8bytes:Length + 4bytes:KeyEvent_CODE
 */
public class KeyboardStrokeModel {
    public int keyCode;
    
    public KeyboardStrokeModel(RawDataModel model)
    {
        ByteBuffer buffer = ByteBuffer.wrap(model.data, 10, 4);
        keyCode = buffer.getInt();
    }
    
    public KeyboardStrokeModel(int keyCode){
        this.keyCode = keyCode;
    }
    
    public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        
        ByteBuffer buffer = ByteBuffer.allocate(1+1+8+4);
        buffer.put(ActionCodes.HEADER_START);
        buffer.put(ActionCodes.KEY_STROKE);
        buffer.putLong(4);
        buffer.putInt(keyCode);
        
        result.length = 4;
        result.data = buffer.array();
        return result;
    }
}

