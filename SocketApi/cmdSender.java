/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.cmdStream;
import SysHttp.Executable;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public class cmdSender extends Sender{
    Executable cmd;
    public cmdSender(IAddress ADDR,Executable cmd) {
        super(ADDR);
        this.cmd = cmd;
    }
    
    @Override
    protected void openStream(Socket LIS){
        
        Timer Reader = new Timer();
        this.TIM = Reader;
        Reader.scheduleAtFixedRate(new cmdStream(this.Recieve,cmd,this.Notifier), 0, 20);
    }
    
}
