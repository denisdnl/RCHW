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
public class MouseRightClickModel {
        public RawDataModel toRawData(){
        RawDataModel result = new RawDataModel();
        
        ByteBuffer data = ByteBuffer.allocate(1+1+8);
        data.put(ActionCodes.HEADER_START);
        data.put(ActionCodes.RIGHT_CLICK);
        data.putLong(0); //0 bytes of payload data since more infos are not important
   
        result.length = 0;
        result.data = data.array();
        return result;
    }
}
