/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.DataReturn;
import SocketApi.IAddress;
import SocketApi.Notifiable;
import SocketApi.Notifier;
import SocketApi.Sender;
import SysHttp.*;
import java.io.IOException;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 * Has 2 connection points, and actively streams to both.
 * One is a Sender, meaning it is made to connect.
 * The other is a socket, as this connection is made solely by a listener.
 */
public class StreamingConnection{
    Sender NetOut;
    DataReturn ConnectorOut;
    Timer Constant = new Timer();
    Notifiable Blank = new Notifier();
    
    public StreamingConnection(IAddress ADDROut,DataReturn ConnectionIn) {
        System.out.println("Creating streamer.");
        NetOut = new Sender(ADDROut);
        
        try{
            NetOut.listen(Blank);
        }catch(Exception E){
            
        }
        ConnectorOut = ConnectionIn;
        Stream();
    }
    
    private Thread StreamFactor(DataReturn Stream,DataReturn Destination){
        Thread D = new Thread(()->{
        if (Stream != null){
            int Id = Stream.getLocalId();
            String Address = Stream.getAddress().toString();
            Notifiable Notify = Stream.getNotifier();
            while (true){
                try{
                    synchronized (Notify){
                        Notify.wait(250); //Heartbeat or every active.
                    }
                }catch(InterruptedException E){
                      
                }
                
                for (String Request : Stream.getNewRequests()){
                    if (Request != null){
                        Destination.Respond(Request);
                        System.out.println(Request);
                    }
                }
                
            }
        }
        });
        return D;
    }
    
    public void Stream(){
        Thread ToWeb = StreamFactor(this.ConnectorOut,this.NetOut.getStreamer(""));
        Thread ToClient = StreamFactor(this.NetOut.getStreamer(""),this.ConnectorOut);
        
        ToWeb.start();
        ToClient.start();
        System.out.println("Started");
        
    }
}
