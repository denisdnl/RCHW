/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import rcclient.codes.ActionCodes;

/**
 *
 * @author Deni-W7
 */
public class ScreenModel {
    public BufferedImage bufferedBitmap;
    
    public ScreenModel(BufferedImage image){
        this.bufferedBitmap = image;
    }
    
    public ScreenModel(RawDataModel model) throws IOException{
        ByteBuffer lengthbuffer = ByteBuffer.wrap(model.data,3,8);
        long length = lengthbuffer.getLong();
        ByteBuffer imagebuffer = ByteBuffer.wrap(model.data,10, (int) length);
        
        InputStream in = new ByteArrayInputStream(imagebuffer.array());
	bufferedBitmap = ImageIO.read(in);
        in.close();
    }
    
    public RawDataModel toRawData() throws IOException{
        RawDataModel result = new RawDataModel();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedBitmap,"png", baos );
        baos.flush();
        byte [] data = baos.toByteArray();
        baos.close();
        ByteBuffer buffer = ByteBuffer.allocate(1+1+8+data.length);
        
        buffer.put(ActionCodes.HEADER_START);
        buffer.put(ActionCodes.GET_SCREEN);
        buffer.putLong(data.length);
        buffer.put(data);
        
        result.length = data.length;
        result.data = buffer.array();
        return result;
    }
}
