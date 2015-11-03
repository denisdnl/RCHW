/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient.services;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rcclient.models.MouseLocationModel;
import utilities.ResolutionConverter;

/**
 *
 * @author Deni-W7
 */
public class MouseLocationService {

    static Timer timer;
    public static final int DELAY = 330;
    public void singletonInit(final JPanel panel){
        if(timer != null)
            return;
        
        timer = new Timer("MouseLocationTimer");
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
               Point p = MouseInfo.getPointerInfo().getLocation();
               SwingUtilities.convertPointFromScreen(p, panel);
               if(p.x>=0 && p.y>=0 && p.x<panel.getWidth() && p.y<panel.getHeight()){
                   ResolutionConverter.convertToRemoteRes(p, panel.getWidth(), panel.getHeight());
                  
                   MouseLocationModel model = new MouseLocationModel(p.x,p.y);
                //   RemoteService.sendAction(model.toRawData());
                    // System.out.println(p.x+" "+p.y);
               }
            }
        }, 2500,DELAY);
    }
}
