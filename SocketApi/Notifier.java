/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author DeadMansMarch
 */
public class Notifier implements Notifiable {
    boolean discon = false;
    boolean Finished = false;
    final HashMap<String,Integer> Streamers = new HashMap<>();
    
    @Override
    public boolean isDisconnected(){
        return this.discon;
    }
    
    @Override
    public void Disconnect(){
        this.discon = true;
    }
    
    @Override
    public boolean isFinished(){
        return Finished;
    }
    
    @Override
    public void NotifyListeners(){
        synchronized (this){
            this.notifyAll();
        }
    }
    
    @Override
    public void Finish(DataReturn Stream,boolean is){
        this.Finished = is;     
    }
    
    @Override
    public String[] getNewStreams(){
        Set<String> K = ((HashMap<String,Integer>) this.Streamers.clone()).keySet();
        int Size = this.Streamers.size();
        this.Streamers.clear();
        
        return K.toArray(new String[Size]);
    }
    
    @Override
    public void StreamStart(String ADDR){
        this.Streamers.put(ADDR, 1);
        this.NotifyListeners();
    }
    
}
