/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JPanel;
import rcclient.remotemachine.RemoteMachineConfig;

/**
 *
 * @author Deni-W7
 */
public class JPanelSmecher extends JPanel {

    public JPanelSmecher(BorderLayout borderLayout) {
        super(borderLayout);
    }

    public JPanelSmecher() {
     
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        if(RemoteMachineConfig.SCREEN != null)
        g.drawImage(RemoteMachineConfig.SCREEN, 0, 0, null);
    }
}
