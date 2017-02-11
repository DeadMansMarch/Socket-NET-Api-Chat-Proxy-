/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SocketApi.DataReturn;
import javax.crypto.SecretKey;

/**
 *
 * @author DeadMansMarch
 * 
 * For use in server. Stores client information.
 */
public class Client {
    protected final String Address;
    protected DataReturn STRM;
    protected String clientName;
    protected Room room = null;
    protected boolean connected = false;
    protected long Last = 0;
    Encryptor ClientEncryptor = new Encryptor("SERVER");
    boolean noExp = false;
    chatServer ChatS;
    public Client(String ADDR,chatServer CHTS){
        this.Address = ADDR;
        this.ChatS = CHTS;
    }
    
    
    public String Encrypt(String K){
        return ClientEncryptor.Encrypt(K);
    }
    
    public String Decrypt(String K){
        return ClientEncryptor.Decrypt(K);
    }
    
    public void setCom(){
        this.Last = System.currentTimeMillis();
    }
    
    public void Connect(){
        this.ChatS.isConnected(this);
    }
    
    public boolean expire(){
        if (this.Last == 0){
            this.Last = System.currentTimeMillis();
        }
        return ((System.currentTimeMillis() - this.Last) > 9000) && !noExp;
    }
    
    public void noExpire(){
        this.noExp = true;
    }
    
    public boolean getConnected(){
        return connected;
    }
    
    public void setConnected(boolean is){
        this.connected = is;
    }
    
    public Room getRoom(){
        return this.room;
    }
    
    public void setRoom(Room newR){
        this.room = newR;
    }
    
    public void setStreamer(DataReturn STRM){
        this.STRM = STRM;
    }
    
    public String getName(){
        return (clientName != null)?clientName : Address;
    }
    
    public void setName(String Name){
        this.clientName = Name;
    }
    
    public boolean send(String toSend){
        if (ClientEncryptor.ready()){
            System.out.println("[Server] Sending client : " + toSend + " with en level " + ClientEncryptor.Using());
            return this.STRM.Respond(ClientEncryptor.Encrypt(toSend));
        }else{
            return this.STRM.Respond(toSend);
        }
    }
    
    public void disconnect(){
        this.STRM.Remove();
        this.STRM.getTimer().cancel();
    }
    
}
