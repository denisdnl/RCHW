/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.services;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
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
import sun.awt.image.ToolkitImage;

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
        t.schedule(this, 2000, 250);
    }

    @Override

    public void run() {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRectangle = new Rectangle(screenSize);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRectangle);
            Image img = image.getScaledInstance(1020,600,3);
            image= new BufferedImage(img.getWidth(null), img.getHeight(null),
            BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            ScreenModel model = new ScreenModel(image);
            RemoteService.sendAction(model.toRawData());
        } catch (AWTException | IOException ex) {
            Logger.getLogger(ScreenService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
