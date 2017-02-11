/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 *
 * @author DeadMansMarch
 */
public class IAddress{
    final private String Location;
    final private int Port;
    
    
    public IAddress(String Location, int Port){
        this.Location = Location;
        this.Port = Port;
    }
    
    public String getNormal(){
        return Location;
    }
    
    public Listener listener() throws IOException{
        return new Listener(this);
    }
    
    public Sender sender(){
        return new Sender(this);
    }
    
    public InetAddress getNet() throws UnknownHostException{
        return InetAddress.getByName(this.Location);
    }
    
    public int getPort(){
        return this.Port;
    }
    
    @Override
    public String toString(){
        return this.Location + ":" + Integer.toString(this.Port);
    }
}
