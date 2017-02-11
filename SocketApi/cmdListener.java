/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.cmdStream;
import SysHttp.Executable;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public class cmdListener extends Listener{
    Executable cmd;
    public cmdListener(IAddress Address,Executable cmd) {
        super(Address);
        this.cmd = cmd;
    }
    
    @Override
    protected void openStream(Socket LIS) throws IOException{
        IAddress NET = new IAddress(LIS.getInetAddress().getHostAddress(),LIS.getPort());
        DataReturn CON = new Streamer(NET,LIS,this);
        Streamers.put(CON.getAddress().toString(), CON);
        
        Timer Reader = new Timer();
        Timers.put(CON.getAddress().toString(),Reader);
        Reader.scheduleAtFixedRate(new cmdStream(CON,cmd,this.Notifier), 0, 100);
    }
    
}
