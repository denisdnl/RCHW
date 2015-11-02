/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.Point;
import rcclient.remotemachine.RemoteMachineConfig;

/**
 *
 * @author Deni-W7
 */
public class ResolutionConverter {
    public static void convertToRemoteRes(Point p,int localW,int localH){
        p.x = (int) ((p.x/(double)localW) * RemoteMachineConfig.SCREEN_WIDTH);
        p.y = (int) ((p.y/(double)localH) * RemoteMachineConfig.SCREEN_HEIGHT);
    }
}
