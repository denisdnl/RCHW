/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import rcserver.models.KeyboardStrokeModel;
import rcserver.models.MouseLeftClickModel;
import rcserver.models.MouseLocationModel;
import rcserver.models.MouseRightClickModel;
import rcserver.models.MouseScrollModel;

/**
 *
 * @author Deni-W7
 */
public abstract class RemoteMachineRecieveCallback {
    public abstract void onCursorLocation(MouseLocationModel mouseLocation); 
    public abstract void onKeyStroke(KeyboardStrokeModel keyStroke); 
    public abstract void onLeftClick(MouseLeftClickModel mouseLeftClick); 
    public abstract void onRightClick(MouseRightClickModel mouseRightClick);
    public abstract void onScroll(MouseScrollModel mouseScroll);
}
