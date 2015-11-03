/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.services;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import rcserver.models.ScreenModel;

/**
 *
 * @author Deni-W7
 */
public class ScreenService extends TimerTask {

    Timer t;

    public void stop() {
        t.cancel();
    }

    public void init() {
        if (t == null) {
            t = new Timer("screenTimer");
        }
        t.schedule(this, 2000, 5000);
    }

    @Override

    public void run() {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            
            ScreenModel model = new ScreenModel(image);
            RemoteService.sendAction(model.toRawData());
        } catch (AWTException | IOException ex) {
            Logger.getLogger(ScreenService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
