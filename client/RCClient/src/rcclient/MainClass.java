/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcclient;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import rcclient.models.KeyboardStrokeModel;
import rcclient.models.MouseLeftClickModel;
import rcclient.models.MouseRightClickModel;
import rcclient.models.MouseScrollModel;
import rcclient.models.RemoteMachineBroadcastCallback;
import rcclient.panels.MainPanel;
import rcclient.remotemachine.RemoteMachineConfig;
import rcclient.services.MouseLocationService;
import rcclient.services.RemoteService;
import rcclient.udplistener.FoundRemoteList;

/**
 *
 * @author Deni-W7
 */
public class MainClass extends JApplet {

    private static final int JFXPANEL_WIDTH_INT = 1024;
    private static final int JFXPANEL_HEIGHT_INT = 600;
    private static JFXPanel fxContainer;
    private static JFrame frame;
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

                frame = new JFrame("Tema RC Client");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JApplet applet = new MainClass();
                applet.init();
                frame.setFocusable(true);
                mainPanel = new MainPanel();
                mainPanel.setFocusable(true);
                
                addGUIListeners(mainPanel);
                frame.setContentPane(applet.getContentPane());
                frame.getContentPane().add(mainPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                applet.start();
                
                FoundRemoteList list = new FoundRemoteList();
                addUDPListener();
            }
            
            
            private void addGUIListeners(MainPanel mainPanel) {
      
                
                Component[] comps = mainPanel.getComponents();
                final JPanel panel1 = (JPanel) comps[0];
                final JTextField ipTf = (JTextField)(((JPanel)comps[1]).getComponents()[1]);
                final JCheckBox cbox = (JCheckBox) (((JPanel)comps[1]).getComponents()[4]);
                final JButton cbtn = (JButton)(((JPanel)comps[1]).getComponents()[2]);
                final JButton dbtn = (JButton)(((JPanel)comps[1]).getComponents()[3]);
      
                final JComboBox combobox = (JComboBox)(((JPanel)comps[1]).getComponents()[5]);
                combobox.removeAllItems();
                combobox.addItemListener( new ItemListener(){

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                      if(e.getStateChange() == ItemEvent.SELECTED)
                          ipTf.setText(e.getItem().toString());
                    }
                });
                cbtn.addMouseListener(new MouseListener(){

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(ipTf.getText().length() == 0){
                            return;
                        }
                        
                         RemoteMachineConfig.IP = ipTf.getText();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
                
                MouseLocationService ms = new MouseLocationService();
                ms.singletonInit(panel1);
                
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {

                    if(ke.getID() ==  KeyEvent.KEY_PRESSED){
                           if(cbox.isSelected()){
                           KeyboardStrokeModel model = new KeyboardStrokeModel(ke.getKeyCode());
                           RemoteService.sendAction(model.toRawData());
//                           System.out.println(ke.getKeyCode());
                           }
                    }                       
                    return false;
                
            }
        });
                
                panel1.addMouseWheelListener(new MouseWheelListener() {

                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {
                        int notches = e.getWheelRotation();
                        if (notches < 0) {
                            MouseScrollModel model = new MouseScrollModel(MouseScrollModel.SCROLL_UP);
                            RemoteService.sendAction(model.toRawData());
                            //   System.out.println("scroll up");
                        } else {
                            MouseScrollModel model = new MouseScrollModel(MouseScrollModel.SCROLL_DOWN);
                            RemoteService.sendAction(model.toRawData());
                            // System.out.println("scroll down"); 
                        }
                    }
                });

                panel1.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            MouseLeftClickModel model = new MouseLeftClickModel();
                            RemoteService.sendAction(model.toRawData());
                            //               System.out.println("left click");
                        }

                        if (SwingUtilities.isRightMouseButton(e)) {
                            MouseRightClickModel model = new MouseRightClickModel();
                            RemoteService.sendAction(model.toRawData());
                            //               System.out.println("right click");
                        }

                        if (SwingUtilities.isMiddleMouseButton(e)) {
                            MouseScrollModel model = new MouseScrollModel(MouseScrollModel.SCROLL_PRESS);
                            RemoteService.sendAction(model.toRawData());
                            //                System.out.println("scroll press");
                        }

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });

            }

            private void addUDPListener() {
                RemoteMachineBroadcastCallback callback = new RemoteMachineBroadcastCallback(){

                    @Override
                    public void onFound(String ip) {
                        if(!FoundRemoteList.hosts.contains(ip)){
                            FoundRemoteList.hosts.add(ip);
                            final JComboBox combobox = (JComboBox)(((JPanel)mainPanel.getComponent(1)).getComponents()[5]);
                           combobox.addItem(ip);
                        }
                        
                    }
                    
                };          
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

            }
        });
    }

}
