/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.image.BufferedImage;

/**
 *
 * @author Deni-W7
 */
public abstract class RemoteScreenCallback {
    public abstract void onScreenUpdated(BufferedImage image);
}
