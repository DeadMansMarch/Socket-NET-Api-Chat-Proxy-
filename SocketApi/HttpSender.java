/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.HttpResponseStream;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public class HttpSender extends Sender{
    
    public HttpSender(IAddress ADDR) {
        super(ADDR);
    }
    
    @Override 
    protected void openStream(Socket LIS){
        Timer Reader = new Timer();
        this.TIM = Reader;
        Reader.scheduleAtFixedRate(new HttpResponseStream(this.Recieve,this.Notifier), 0, 20);
    }
}
