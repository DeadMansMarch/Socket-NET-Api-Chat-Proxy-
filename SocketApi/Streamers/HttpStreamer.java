/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Streamers;

import SocketApi.DataReturn;
import SocketApi.IAddress;
import SocketApi.Notifiable;
import SocketApi.StreamingConnection;
import SocketApi.StreamingListener;
import SysHttp.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author DeadMansMarch
 */
public class HttpStreamer extends baseStream{
    StreamingConnection sendMyself;
    public HttpStreamer(DataReturn CON, Notifiable Notifier) {
        super(CON, Notifier);
    }
    
    Request K = new Request();
    
    @Override
    public void run(){
        try{
            if (true){
                boolean EndTimer = false;
                if (CON.getStream().isClosed()){
                    EndTimer = true;
                }
                String read = BUFF.readLine();
                if (EndTimer){
                    CON.getTimer().cancel();
                    CON.Remove();
                    this.UPD();
                    return;
                }
                if (read != null){
                    K.Append(read);
                    if (K.getHost().getPort() > 0){
                        System.out.println("This streamer has a return address of : " + this.CON.getAddress());
                        sendMyself = new StreamingConnection(K.getHost(),CON);
                    }
                    CON.Streamed(read);
                    this.UPD();
                }
              
            }
                
        }catch(IOException E){
            CON.getTimer().cancel();
            CON.Remove();
                
            System.out.println("Timer Error [id = " + this.CON.getLocalId() + "] :" + E);
        }
    }
    
    
}
