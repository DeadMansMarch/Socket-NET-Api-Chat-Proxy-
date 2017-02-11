/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.baseStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public class StreamingListener extends Listener{
    IAddress Location;
    public StreamingListener(IAddress ADDR,IAddress Location){
        super(ADDR);
        this.Location = Location;
    }
    
    @Override
    protected void openStream(Socket LIS) throws IOException{
        IAddress NET = new IAddress(LIS.getInetAddress().getHostAddress(),LIS.getPort());
        DataReturn CON = new Streamer(NET,LIS,this);
        Streamers.put(CON.getAddress().toString(), CON);
        
        Timer Reader = new Timer();
        Timers.put(CON.getAddress().toString(),Reader);
        Reader.scheduleAtFixedRate(new baseStream(CON,this.Notifier), 0, 100);
        StreamingConnection TAC = new StreamingConnection(new IAddress("192.168.0.104",8080),CON);
    }
}
