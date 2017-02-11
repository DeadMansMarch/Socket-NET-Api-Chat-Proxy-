/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.util.ArrayList;


public class Room {
    public static int RoomNum = 0;
    public boolean PassSet = false;
    protected String Name = "";
    protected String Password = ""; //Hashed.
    protected String Creator = ""; // An address.
    protected int roomNumber = 0;
    protected ArrayList<Client> Pending = new ArrayList<>(); //Users waiting for password creation.
    protected ArrayList<Client> Active = new ArrayList<>(); //Users in server.
    protected int MinLayers = 1;
    
    public Room(){
        Room.RoomNum += 1;
        this.roomNumber = Room.RoomNum;
    }
    
    public void setMinLayers(int Min){
        this.MinLayers = Min;
    }
    
    public int getMinLayers(){
        return this.MinLayers;
    }
    
    public String getName(){
        return Name;
    }
    
    public void setName(String name){
        this.Name = name;
    }
    
    public int roomNumber(){
        return this.roomNumber;
    }
    
    public boolean passwordSet(){
        return this.PassSet;
    }
    
    public int Active(){
        return this.Active.size();
    }
    
    public int Pending(){
        return this.Pending.size();
    }
    
    public String formatIn(Client self){
        String Ret = "";
        for (Client Addr : Active){
            if (Addr != self){
                Ret += " " + (Addr.Address + ">" + Addr.getName());
            }
        }
        return Ret;
    }
    
    public void addClient(Client New){
        this.Active.add(New);
        New.setConnected(true);
    }
    
    public void pendClient(Client New){
        this.Pending.add(New);
    }
    
    public void Activate(Client Cli){
        Cli.setConnected(true);
        this.Pending.remove(Cli);
        this.Active.add(Cli);
    }
    
    public String getPassword(){
        return Password;
    }
    
    public void notifyPending(){
        for (Client Cli : Pending){
            if (this.PassSet && !this.Password.equals("")){
                System.out.println("NOTIFY : " + Cli.Address);
                Cli.send("rp");
            }else{
                Cli.Connect();
            }
        }
    }
    
    public void setPassword(String Password){
        this.Password = Encryptor.hashToString(Encryptor.hash(Password));
        this.PassSet = true;
        this.notifyPending();
    }
    
    public void setCreator(String Creator){
        this.Creator = Creator;
    }
    
    public void remove(Client Cli){
        Active.remove(Cli);
        Pending.remove(Cli);
    }
    
    public void send(Client self,String toSend){
        for (Client k : Active){
            if (k != self){
                k.send(toSend);
            }
        }
    }
    
    public void shutdown(){
        
    }
}


