/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.services;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import rcserver.models.RemoteResolutionModel;

/**
 *
 * @author Deni-W7
 */
public class ResolutionService extends TimerTask {

    private Timer t;
    
    public void init(){
        if(t == null)
            t = new Timer("resolutionTimer");
    //    t.schedule(this, 1000, 10000);
    }
    
    @Override
    public void run() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        RemoteResolutionModel model = new RemoteResolutionModel((int)screenSize.getWidth(),(int)screenSize.getHeight());
        RemoteService.sendAction(model.toRawData());
    }

    public void stop() {
        t.cancel();
    }
    
}
