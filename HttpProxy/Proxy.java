/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HttpProxy;

import SocketApi.*;
import SysHttp.Request;
import SysHttp.RequestStorage;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 * A more organized version of the original web intercept.
 */
public class Proxy {
    private final Interactor Listener;
    private final Notifiable Notifier = new Notifier();
    private final RequestStorage Requests = new RequestStorage();
    private Thread Interceptor;
    private final HashMap<String,DataReturn> Connected = new HashMap<>();
    
    
    public static void main(String[] Args){
        
        Proxy Main = new Proxy(new IAddress("192.168.0.104",8888));
        Main.start();
    }

    public Proxy(IAddress ep){
        //Listener = new HttpListener(ep);
        System.out.println("New listener created");
        Listener = new HttpStreamListener(new IAddress("192.168.0.104",8888));
    }
    
    public void start(boolean T){
        
        
    }
    
    public void start(){
        System.out.println("Proxy startup.");
        try{
            //Notifiable n = new Notifier();
            //Sender Sock = new Sender(new IAddress("stackoverflow.com",80));
            //Sock.listen(n);
            //Sock.send(new String[]"http://stackoverflow.com/questions/13057240/waitlong-timeout-in-a-while-loop HTTP/1.1"
            //       + "Host: insidetrust.blogspot.com\n");
            //System.out.println(Intercept(Sock.getStreamer(""),n));
            this.Listener.listen(this.Notifier);
            
            Stream();
        }catch(Exception E){
            
        }
    }
    
    private Request Intercept(DataReturn Stream){
        if (Stream != null){
            Request Dat = new Request();
            int Id = Stream.getLocalId();
            String Address = Stream.getAddress().toString();
            Notifiable Notif = Stream.getNotifier();
            while (true){
                try{
                    synchronized (Notif){
                        Notif.wait(250); //Heartbeat or every active.
                    }
                }catch(InterruptedException E){
                      
                }
                
                
                for (String Request : Stream.getNewRequests()){
                    if (Request != null){
                        Dat.Append(Request);
                        System.out.println("[id = " + Id + "] Data appended");
                    }
                }
                
                
                if (Notif.isFinished()){
                    System.out.println("Received SIG finish [id = " + Id + "]");
                    return Dat;
                }
            }
        }
        System.out.println("Stream null. Returning.");
        return null;
    }
    
    public void FullStreamIntercept(DataReturn Stream){
        int Id = Stream.getLocalId();
        
        Request Data = Intercept(Stream);
        System.out.println(Data.toString());
        Sender WWW = new HttpSender(Data.getHost());
        
        try{
            
            WWW.listen(new Notifier());
            WWW.send(Data.getLines());

            Request Response = Intercept((Streamer) WWW.getStreamer(null));
            System.out.println(Response);
            String[] Lines = Response.getLines();
            System.out.println(Lines.length);
            Stream.Respond(Lines);
            System.out.println("Returned");
            
        }catch(Exception E){
            
        }
        
    }
    
    public void Stream(){
        while (true){
            try{
                sleep(2000);
                
            }catch(InterruptedException E){
                
            }
        }
    }
    
}

