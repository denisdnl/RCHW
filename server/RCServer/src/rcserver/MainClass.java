/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import rcserver.models.KeyboardStrokeModel;
import rcserver.panels.MainPanel;
import rcserver.udpbroadcast.UDPBroadcast;
import rcserver.models.LocalInterfaceModel;
import rcserver.models.MouseLeftClickModel;
import rcserver.models.MouseLocationModel;
import rcserver.models.MouseRightClickModel;
import rcserver.models.MouseScrollModel;
import rcserver.services.RemoteService;
import utilities.RemoteMachineRecieveCallback;

/**
 *
 * @author Deni-W7
 */
public class MainClass extends JApplet {
    
    private static final int JFXPANEL_WIDTH_INT = 392;
    private static final int JFXPANEL_HEIGHT_INT = 130;
    private static JFXPanel fxContainer;
    
    private static MainPanel mainPanel;
    
    private static UDPBroadcast udpbroadcast;
    private static RemoteService tcpservice;

    static LocalInterfaceModel currentInterface;
    static JComboBox interfaceCombo;
    
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
                
                Enumeration<NetworkInterface> networkInterface;
                try {
                    networkInterface = NetworkInterface.getNetworkInterfaces();
                    while(networkInterface.hasMoreElements()) {
                        NetworkInterface ni = networkInterface.nextElement();
                        for(InterfaceAddress iAddr : ni.getInterfaceAddresses()) {
                            if(iAddr.getBroadcast() != null) {
                                interfaceCombo.addItem(new LocalInterfaceModel(ni.getName(),iAddr.getAddress(),iAddr.getBroadcast()));
                            }
                        }
                    }
                } catch (SocketException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                udpbroadcast = new UDPBroadcast();
            }
            
            private void addGUIListeners(MainPanel mainPanel) {
                Component[] comps = mainPanel.getComponents();
                final JLabel statusLabel = (JLabel)comps[2];
                final JLabel myIPLabel = (JLabel)comps[1];
                final JButton stopButton = (JButton)comps[4];
                final JButton startButton = (JButton)comps[3];
                final JTextField nameField = (JTextField)comps[5];
                interfaceCombo = (JComboBox)comps[6];
                
                System.out.println(nameField.getText());
                
                interfaceCombo.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent ie) {
                        if(ie.getStateChange() == ItemEvent.SELECTED) {
                            currentInterface = (LocalInterfaceModel) ie.getItem();
                            myIPLabel.setText(currentInterface.address.toString());
                        }
                    }
                });
                
                startButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        //@TODO handle clickey!
                        udpbroadcast.start(currentInterface.broadcastAddress, nameField.getText().toString());
                        statusLabel.setText("Broadcasting on UDP!");
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
                        udpbroadcast.stop();
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
            
            private void addTCPListener() throws IOException {
                RemoteMachineRecieveCallback callback = new RemoteMachineRecieveCallback() {
                    @Override
                    public void onCursorLocation(MouseLocationModel mouseLocation) {
                    }

                    @Override
                    public void onKeyStroke(KeyboardStrokeModel keyStroke) {
                    }

                    @Override
                    public void onLeftClick(MouseLeftClickModel mouseLeftClick) {
                        System.out.println("CLICKLETYTTT!");
                    }

                    @Override
                    public void onRightClick(MouseRightClickModel mouseRightClick) {
                    }

                    @Override
                    public void onScroll(MouseScrollModel mouseScroll) {
                    }
                    
                };      
                tcpservice = new RemoteService();
                try {
                    tcpservice.start(callback, currentInterface.address);
                } catch (SocketException ex) {
                    Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
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
