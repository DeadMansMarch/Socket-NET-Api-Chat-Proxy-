/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Streamers;

import SocketApi.DataReturn;
import SocketApi.Notifiable;
import java.io.IOException;

/**
 *
 * @author DeadMansMarch
 */
public class HttpRequestStream extends baseStream{
    boolean Readable = false;
            
    public HttpRequestStream(DataReturn CON, Notifiable Notifier) {
        super(CON, Notifier);
    }
    
    @Override
    public void run(){
        try{
            if (this.INPR.ready()){
                Readable = true;
                this.Notifier.Finish(CON, false);
            }
            
            if (Readable){
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
                if (read == null || read.equals("")){
                    Readable = false;
                    this.Finish(true);
                    
                }else{
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