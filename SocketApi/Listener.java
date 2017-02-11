
package SocketApi;

import SocketApi.Streamers.baseStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author DeadMansMarch
 * 
 * This class is mainly for use in servers - it is for waiting for and acknowledging 
 * client connections.
 */



public class Listener implements Interactor{
    protected final HashMap<String,DataReturn> Streamers = new HashMap<>();
    protected final HashMap<String,Timer> Timers = new HashMap<>();
    
    protected IAddress ADDR;
    protected ServerSocket Listener;
    protected Notifiable Notifier;
    
    public Listener(IAddress Address){
        this.ADDR = Address;
        this.linkTo(this.ADDR);
    }
 
    protected static class Intercept implements Runnable{
        Listener Parent;
        Intercept(Listener Parent){
            this.Parent = Parent;
        }
        @Override
        public void run() {
            while (true){
                try{
                    Socket Listen = this.Parent.Listener.accept();
                    this.Parent.openStream(Listen);
                }catch(IOException E){
                    System.out.println("Except : " + Parent.ADDR.toString());
                }
            }
        }
    }
    
    
    public Set<String> streamers(){
        return Streamers.keySet();
    }
    
    @Override
    public DataReturn getStreamer(String Key){
        return Streamers.get(Key);
    }
    
    @Override
    public Timer getTimer(String ADDR){
        return Timers.get(ADDR);
    }
    
    protected void linkTo(IAddress ADDR){
        if (this.Listener != null){
            try{
                this.Listener.close();
            }catch(IOException E){
                System.out.println(E);
            }
        }
        
        try{
            this.Listener = makeSocket(ADDR,1000);
        }catch(IOException E){
            System.out.println(E);
            System.out.println("Fatal linking error.");
            System.exit(9);
        }
    }
    
    protected static ServerSocket makeSocket(IAddress ADDR,int Backlog) throws IOException{
        return new ServerSocket(ADDR.getPort(),Backlog,ADDR.getNet());
    }
    
    public void link(IAddress ADDR){
        this.linkTo(ADDR);
    }
    
    @Override
    public void removeStreamer(String Key){
        this.Streamers.remove(Key);
    }
    
    @Override
    public void listen(Notifiable Notifier) throws IOException{
        this.Notifier = Notifier;
        Thread EarPiece = new Thread(new Intercept(this));
        EarPiece.start();
    }
    
    protected void openStream(Socket LIS) throws IOException{
        IAddress NET = new IAddress(LIS.getInetAddress().getHostAddress(),LIS.getPort());
        DataReturn CON = new Streamer(NET,LIS,this);
        Streamers.put(CON.getAddress().toString(), CON);
        
        Timer Reader = new Timer();
        Timers.put(CON.getAddress().toString(),Reader);
        Reader.scheduleAtFixedRate(new baseStream(CON,this.Notifier), 0, 100);
        
    }
}
