/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public class Streamer implements DataReturn {
    static int Id = 0;
    final private IAddress ADDR;
    final private Socket Stream;
    final private Interactor Parent;
    final private Notifiable Notifier = new Notifier();
    final private int privateId;
    private DataOutputStream SEN;
    final private ArrayList<String> Requests = new ArrayList<>(1000);
    private BufferedOutputStream BOS;
    int Marker = 0;
    int Req = 0;
    
    public synchronized static int IncId(){
        Streamer.Id += 1;
        return Streamer.Id;
    }
    
    public Streamer(IAddress ADDR,Socket STRM,Interactor Parent){
        this.ADDR = ADDR;
        this.Stream = STRM;
        this.Parent = Parent;
        
        this.privateId = Streamer.IncId();
        try{
            this.SEN = new DataOutputStream(this.Stream.getOutputStream());
            this.BOS = new BufferedOutputStream(this.SEN);
        }catch(IOException E){
            
        }
        
    }
    
    @Override
    public int getLocalId(){
        return this.privateId;
    }
    
    @Override
    public Socket getStream(){
        return Stream;
    }
    
    @Override
    public synchronized boolean Respond(String Data){
        try{
            
            this.SEN.writeBytes(Data + "\n");
            this.SEN.flush();
            return true;
            
        }catch(IOException E){
            System.out.println(E);
            return false;
        }
    }
    
    @Override
    public boolean Respond(String[] Data){
        try{
            if (this.SEN == null){
                this.SEN = new DataOutputStream(this.Stream.getOutputStream());
            }
            for (String Dat : Data){
                this.SEN.writeBytes(Dat + "\n");
                
            }
            this.SEN.flush();
            return true;
            
        }catch(IOException E){
            return false;
        }
    }
    
    @Override
    public String[] getNewRequests(){
        ArrayList<String> RequestL = (ArrayList<String>) this.Requests.clone();
        int Size = RequestL.size();
        if (Marker < Size){
            String[] New = new String[Size];
            ListIterator<String> K = RequestL.listIterator(Marker);
            for (int i = Marker;i<Size;i++){
                String Val = K.next();
                New[i - Marker] = Val;
            }
            Marker = Size;
            return New;
        }
        
        return new String[1];
    }
    
    @Override
    public void Remove(){
        this.Parent.removeStreamer(this.ADDR.toString());
        try{
            this.Stream.close();
        }catch(IOException E){
            
        }
    }
    
    @Override
    public String getPClass(){
        return this.Parent.getClass().toString();
    }
    
    @Override
    public IAddress getAddress(){
        return ADDR;
    }
    
    @Override
    public void Streamed(String STRM){
        synchronized (Requests){
            Req += 1;
            Requests.add(STRM);
        }
    }
    
    @Override
    public Notifiable getNotifier(){
        return this.Notifier;
    }
    
    @Override
    public Timer getTimer(){
        return this.Parent.getTimer(this.ADDR.toString());
    }
    
}
