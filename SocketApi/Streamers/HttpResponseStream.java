/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Streamers;

import SocketApi.DataReturn;
import SocketApi.Notifiable;
import SocketApi.Streamers.baseStream;
import java.io.IOException;

/**
 *
 * @author DeadMansMarch
 */
public class HttpResponseStream extends baseStream{
    boolean Readable = false;
    boolean Headers = false;
    boolean ContentHeader = false;
    int CLength = 0;
    int CurrentLength = 0;
    
    public HttpResponseStream(DataReturn CON, Notifiable Notifier) {
        super(CON, Notifier);
    }
    
    @Override
    public void run(){
        try{

            if (this.INPR.ready()){
                Readable = true;
                this.Finish(false);
            }
            
            if (Readable){
                boolean EndTimer = false;
                if (CON.getStream().isClosed()){
                    EndTimer = true;
                }
                int k = BUFF.read();
                System.out.println(k);
                String read = BUFF.readLine();
                if (EndTimer){
                    CON.getTimer().cancel();
                    CON.Remove();
                    this.UPD();
                    return;
                }
                
                if (!Headers){
                    if (read.equals("")){
                        Headers = true;
                        System.out.println("Headers received");
                    }
                }
                
                if (!Headers){
                    if (read.contains("Content-Length: ")){
                        int Index = read.indexOf("Content-Length: ") + 16;
                        this.CLength = Integer.parseInt(read.substring(Index));
                        this.ContentHeader = true;
                    }
                }else{
                    if (ContentHeader){
                        CurrentLength += read.length();
                    }
                }
                       
                
                if (read == null || CurrentLength >= this.CLength){
                    Readable = false;
                    this.Finish(true);
                }else{
                    CON.Streamed((char) k + read);
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
