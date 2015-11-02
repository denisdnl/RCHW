/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import rcserver.panels.MainPanel;

/**
 *
 * @author Deni-W7
 */
public class MainClass extends JApplet {
    
    private static final int JFXPANEL_WIDTH_INT = 374;
    private static final int JFXPANEL_HEIGHT_INT = 66;
    private static JFXPanel fxContainer;
    
    private static MainPanel mainPanel;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("Tema RC Client");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JApplet applet = new MainClass();
                applet.init();
                mainPanel = new MainPanel();
                mainPanel.setFocusable(true);
                
                addGUIListeners(mainPanel);
                
                frame.setContentPane(applet.getContentPane());
                frame.getContentPane().add(mainPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                applet.start();
            }
            
            private void addGUIListeners(MainPanel mainPanel) {
                Component[] comps = mainPanel.getComponents();
                final JLabel statusLabel = (JLabel)comps[0];
                final JLabel myIPLabel = (JLabel)comps[2];
                final JButton stopButton = (JButton)comps[4];
                final JButton startButton = (JButton)comps[3];
                
                startButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        //@TODO handle clickey!
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }
                });
                
                stopButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        //@TODO handle clickey!
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }
                });
            }
        });
    }
    
    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene() {

    }
    
}
