/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcserver.models;

import java.net.InetAddress;

/**
 *
 * @author flash
 */
public class LocalInterfaceModel {
    public String interfaceName;
    public InetAddress address;
    public InetAddress broadcastAddress;
    
    public LocalInterfaceModel(String interfaceName, InetAddress address, InetAddress broadcastAddress) {
        this.interfaceName = interfaceName;
        this.address = address;
        this.broadcastAddress = broadcastAddress;
    }
    
    @Override
    public String toString() {
        return this.interfaceName;
    }
}
