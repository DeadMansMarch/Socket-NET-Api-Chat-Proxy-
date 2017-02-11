/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.HttpStreamer;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 * Technically the same as a streaming connection EXCEPT
 * it will stream itself to the address given in each request.
 */
public class HttpStreamListener extends Listener{
    public HttpStreamListener(IAddress ADDR){
        super(ADDR);
    }
    
    //My mind is frying.
    //HttpStreamer will take the connection and relay it to a StreamingConnection with the requested host.
    @Override
    protected void openStream(Socket LIS) throws IOException{
        IAddress NET = new IAddress(LIS.getInetAddress().getHostAddress(),LIS.getPort());
        DataReturn CON = new Streamer(NET,LIS,this);
        Streamers.put(CON.getAddress().toString(), CON);
        
        Timer Reader = new Timer();
        Timers.put(CON.getAddress().toString(),Reader);
        Reader.scheduleAtFixedRate(new HttpStreamer(CON,this.Notifier), 0, 100);
        
    }
}
