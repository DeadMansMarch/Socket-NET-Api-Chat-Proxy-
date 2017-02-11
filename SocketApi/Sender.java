/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.Streamers.baseStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 * 
 * This class is meant to be a "reach out" system. While a listener can wait and
 * respond to data, the sender is built to reach out and receive on only one
 * comms path.
 * 
 */

public class Sender implements Interactor{
    final protected IAddress ADDR;
    protected Socket COM;
    protected DataOutputStream SEN;
    protected DataReturn Recieve;
    protected Notifiable Notifier;
    protected Timer TIM;
    
    public Sender(IAddress ADDR){
        this.ADDR = ADDR;
        this.linkTo(ADDR);
    }

    
    protected void linkTo(IAddress ADDR){
        if (this.COM != null){
            try{
                this.COM.close();
            }catch(IOException E){
                System.out.println(E);
            }
        }
        
        try{
            this.COM = makeSocket(ADDR.getNormal(),ADDR.getPort());
            IAddress NET = new IAddress(COM.getInetAddress().getHostAddress(),COM.getPort());
            this.Recieve = new Streamer(NET,COM,this);
        }catch(IOException E){
            System.out.println(E);
            System.out.println("Fatal linking error: ADDR: " + ADDR.toString());
            //System.exit(8);
        }
    }
    
    public static Socket makeSocket(String ADDR,int Port) throws IOException{
        System.out.println("Making new socket, Addr:" + ADDR + " Port: " + Port);
        return new Socket(ADDR, Port);
    }
    
    public static Socket makeSocket(IAddress ADDR) throws IOException{
        System.out.println("Making new socket, Addr:" + ADDR);
        return new Socket(ADDR.getNormal(), ADDR.getPort());
    }
    
    public void link(IAddress ADDR){
        linkTo(ADDR);
    }
    
    @Override
    public void removeStreamer(String Empt) {
        try{
            this.COM.close();
        }catch(IOException E){
            
        }
        
    }
    
    @Override
    public DataReturn getStreamer(String K) {
        return Recieve;
    }

    @Override
    public Timer getTimer(String Timer) {
        return this.TIM;
    }
    
    public void send(String Data){
        try{
            if (this.SEN == null){
                this.SEN = new DataOutputStream(this.COM.getOutputStream());
            }
            
            this.SEN.writeBytes(Data);
            this.SEN.writeBytes("\n");
            this.SEN.flush();
            
            
        }catch(IOException E){
            
        }
    }
    
    public void send(String[] Data){
        try{
            if (this.SEN == null){
                this.SEN = new DataOutputStream(this.COM.getOutputStream());
            }
            for (String Dat : Data){
                this.SEN.writeBytes(Dat);
                
            }
            
            this.SEN.writeBytes("\n");
            this.SEN.flush();
            
            
        }catch(IOException E){
            
        }
    }
    
    @Override
    public void listen(Notifiable Notifier) throws Exception{
        
        this.Notifier = Notifier;
        if (this.COM != null){
            openStream(this.COM);
        }else{
            throw new NullSocketException();
        }
    }
    
    protected void openStream(Socket LIS){
        
        Timer Reader = new Timer();
        this.TIM = Reader;
        Reader.scheduleAtFixedRate(new baseStream(this.Recieve,this.Notifier), 0, 20);
    }
}
