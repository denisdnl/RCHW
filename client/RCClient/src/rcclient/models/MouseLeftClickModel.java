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
 * StartByte + ACtionByte + 4Bytes:Length
 */
public class MouseLeftClickModel {
    
    public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        
        ByteBuffer data = ByteBuffer.allocate(6);
        data.put(ActionCodes.HEADER_START);
        data.put(ActionCodes.LEFT_CLICK);
        data.putLong(0); //0 bytes of payload data since more infos are not important
   
        result.length = 0;
        result.data = data.array();
        return result;
    }
    
}
