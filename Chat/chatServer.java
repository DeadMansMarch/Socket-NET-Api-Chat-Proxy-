/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SocketApi.IAddress;
import SocketApi.Notifiable;
import SocketApi.Notifier;
import SocketApi.cmdListener;
import SysHttp.Executable;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import javax.crypto.SecretKey;

/**
 *
 * @author DeadMansMarch
 * 
 * For use as main class. Will run server.
 */
public class chatServer {
    protected final cmdListener Bouncer;
    protected final Notifiable Notifier = new Notifier();
    protected final HashMap<String,Client> clientList = new HashMap<>();
    protected final chatServer Reference = this;
    protected final Random ServerRandom = new Random();
    protected final String Version = "BETA0.2";
    protected final HashMap<Integer,Room> Rooms = new HashMap<>();
    protected final HashMap<String,Integer> nameToNumber = new HashMap<>();
    protected final String[] prmList = new String[]{"2917","1109","2003","2017"
            ,"2459","2437","7753","16381","24421","19801","14851","19937","11939","2311","5039"};
    
    protected final BigInteger[] ServerPrimes = new BigInteger[prmList.length];
    
    //Diffie Hellman A.
    private String DiffHA(Encryptor E){
        String g = Integer.toString(ServerRandom.nextInt(100));
        String p = ServerPrimes[ServerRandom.nextInt(6000) % ServerPrimes.length].toString();
        String PRI = Integer.toString(ServerRandom.nextInt(4000));
        E.setVar("g", g);
        E.setVar("p", p);
        E.setVar("PRI", PRI);
        return "evars " + g + " " + p;
    }
    
    public boolean testDiffieHellman(){
        Encryptor EN1 = new Encryptor("ServerEncrytor");
        Encryptor EN2 = new Encryptor("ClientEncryptor");
        //Server start.
        String g = Integer.toString(ServerRandom.nextInt(5000));
        String p = ServerPrimes[ServerRandom.nextInt(6000) % ServerPrimes.length].toString();
        String PRI = Integer.toString(ServerRandom.nextInt(2000));
        
        EN1.setVar("g", g);
        EN1.setVar("p", p);
        EN1.setVar("PRI", PRI);
        
        //Client start.
        String PRI2 = Integer.toString(ServerRandom.nextInt(2000));
        EN2.setVar("g", g);
        EN2.setVar("p",p);
        EN2.setVar("PRI", PRI2);
        
        String ENt1 = EN2.passFunc(EN2.getVars(new String[]{"g","p","PRI"}));
        //Server restart, receive ENt1.
        EN1.setVar("ENt1", ENt1);
        String ENt2 = EN1.passFunc(EN1.getVars(new String[]{"g","p","PRI"}));
        String ServerPri = EN1.passFunc(EN1.getVars(new String[]{"ENt1","p","PRI"}));
        //Client restart, receive ENt2.
        EN2.setVar("ENt2",ENt2);
        String ClientPri = EN2.passFunc(EN2.getVars(new String[]{"ENt2","p","PRI"}));
        return ServerPri.equals(ClientPri);
    }
    
    public void isConnected(Client Cli){
        Cli.getRoom().Activate(Cli);
        send(Cli,"CONNECTED " + Version + " " + Cli.getRoom());
        send(Cli,"announce There are [" + Cli.getRoom().Active() + "] people connected to this room.");
        sendRoom(Cli,"UDCA " + Cli.Address + " " + Cli.getName());
    }
    
    void connectionVet(String Address,String Data){
        Client Cli = getClient(Address);
        String[] Split = Data.split(" ");
        if (!Cli.expire()){
            switch(Split[0].toLowerCase()){ // LOWERCASE!!
                case "connect":
                    log("Connection candidate: " + Address);
                    if (Data.split("/").length >= 3){
                        String Rm = Data.split("/")[1];
                        try{
                            if (Rm.length() > 2){
                                String Roomname = Rm;
                                if (this.nameToNumber.get(Roomname) == null){
                                    Room creation = new Room();
                                    creation.setCreator(Cli.Address);
                                    int num = Room.RoomNum;
                                    creation.setName(Roomname);
                                    this.Rooms.put(num,creation);
                                    this.nameToNumber.put(Roomname, num);
                                    
                                    log("Created new room under name " + Roomname);
                                }
                                Room clientRoom = this.Rooms.get(this.nameToNumber.get(Roomname));
                                Cli.setRoom(clientRoom);
                                this.send(Cli, "VER");
                            }
                            
                        }catch(Exception E){
                            log(E.toString());
                            Cli.disconnect();
                            break;
                        }
                        
                    }else{
                        this.disconnect(Cli);
                    }
                    break;
                case "ver":
                    log("Version was received.");
                    if (Split[1].equals(this.Version)){
                        SecretKey Key = Cli.ClientEncryptor.generateSecret();
                        send(Cli,"encrypt-length " + Encryptor.keyToString(Key));
                        Cli.ClientEncryptor.addLayer(Key);
                    }else{
                        send(Cli,"UPG");
                        Cli.disconnect();
                    }
                    break;
                default:
                    if (!Cli.ClientEncryptor.ready()){
                        this.disconnect(Cli);
                    }else{
                        String[] Dat = Cli.Decrypt(Data).split(" ");
                        switch(Dat[0]){
                            case "enumv":
                                Cli.send(DiffHA(Cli.ClientEncryptor));
                                break;
                            case "bvars":
                                Encryptor EN1 = Cli.ClientEncryptor;
                                
                                Cli.ClientEncryptor.setVar("ENt1",Dat[1]);
                                Cli.send("bvars " + EN1.passFunc(EN1.getVars(new String[]{"g","p","PRI"})));
                                EN1.privatize(EN1.passFunc(EN1.getVars(new String[]{"ENt1","p","PRI"})));
                                
                                break;
                            case "attempt":
                                log("CLIENT VETTED");
                                Cli.noExpire();
                                Room Room = Cli.getRoom();
                                String Pass = Room.getPassword();
                                if (Room.passwordSet() && !Pass.equals("")){
                                    send(Cli,"rp");
                                }else if (!Room.passwordSet() && Room.Creator.equals(Cli.Address)){
                                    send(Cli,"sp");
                                }else if (!Room.passwordSet()){
                                    this.send(Cli, "OWNW");
                                    Room.pendClient(Cli);
                                }else if (Room.passwordSet() && Pass.equals("")){
                                    isConnected(Cli);
                                }
                                
                                break;
                            case "rp":
                                log("RP");
                                String SentPass = Encryptor.hashToString(Encryptor.hash(CatDrop(Split,1)));
                                String RoomPass = Cli.getRoom().getPassword();
                                log(RoomPass + " : " + SentPass);
                                if (!RoomPass.equals("")){
                                    if (RoomPass.equals(SentPass)){
                                        isConnected(Cli);
                                    }else{
                                        this.disconnect(Cli);
                                    }
                                }else{
                                    isConnected(Cli);
                                }
                                
                                
                                break;
                            case "sp":
                                log("RP");
                                String newPass = CatDrop(Split,1);
                                Room room = Cli.getRoom();
                                if (room != null && !room.passwordSet()){
                                    room.setPassword(newPass);
                                }
                                isConnected(Cli);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
            }
            Cli.setCom();
        }else{
            this.disconnect(Cli);
        }
    }
    
    public void log(String K){
        System.out.println("[Server] " + K);
    }
    
    private void connectionApply(String Address, String Data){
        Client Cli = this.getClient(Address);
        String TrueData = Cli.Decrypt(Data);
        String[] Split = TrueData.split(" ");
        switch(Split[0]){
            case "name":
                this.sendRoom(Cli, "UDCN " + Cli.Address + " " + CatDrop(Split,1));
                Cli.setName(Split[1]);
                break;
            case "UDUL":
                
                Cli.send("CU" + Cli.getRoom().formatIn(Cli));
                break;
            default:
                log("Default");
                this.sendRoom(Cli,"message " + Cli.Address + " " + CatDrop(Split,1));
                break;
        }
        
    }
    
    private String CatDrop(String[] Split,int Start){
        String Dat = "";
        for (int i = Start;i<Split.length;i++){
            Dat += Split[i];
            if (i != Split.length - 1){
                Dat += " ";
            }
                  
        }
        return Dat;
    }

    private class Executer extends Executable{
        chatServer Reference;
        public Executer(chatServer Ref){
            this.Reference = Ref;
        }
        
        @Override
        public void run(String Address,String Data){
            Client K = Reference.getClient(Address);
            if (K==null){
                K = makeClient(Address);
            }
            if (!K.getConnected()){
                log("Vet");
                connectionVet(Address,Data);
            }else{
                log("Apply");
                connectionApply(Address,Data);
            }
        }
    }
    
    public chatServer(IAddress Location){
        for (int i = 0;i<prmList.length;i++){
            ServerPrimes[i] = new BigInteger(prmList[i]);
        }
        
        Bouncer = new cmdListener(Location,new Executer(this));
        
        try{
            Bouncer.listen(Notifier);
        }catch(IOException E){
            
        }
        monitor();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                this.sendAll("announce SERVER SHUTDOWN. STREAM END.");
            }catch(Exception E){
                
            }
        }));
    }
    
    protected void monitor(){
        Thread K = new Thread(()->{
        while (true){
            try{
                synchronized (Notifier){
                    Notifier.wait(1000);
                }
                
                for (String ID : Notifier.getNewStreams()){
                    if (clientList.get(ID) == null){
                        makeClient(ID);
                    }
                }
                
                ArrayList<Client> Remove = new ArrayList<>();
                for (String CLIAddr : this.clientList.keySet()){
                    Client CLI = getClient(CLIAddr);
                    if (!CLI.send("LI" + ServerRandom.nextInt(1000) + "VE")){
                        Remove.add(CLI);
                    }
                }
                
                for (Client CLI : Remove){
                    this.disconnect(CLI);
                }
                
            }catch(InterruptedException E){
                
            }
        }
        });
        
        K.start();
    }
    
    public Client makeClient(String ADDR){
         Client N = new Client(ADDR,this);
         N.setStreamer(Bouncer.getStreamer(ADDR));
         clientList.put(ADDR,N);
         return N;
    }
    
    public Client getClient(String Address){
        return this.clientList.get(Address);
    }
    
    public void sendAll(String toSend){
        clientList.keySet().forEach((i)->{
            clientList.get(i).send(toSend);
        });
    }
    
    public void sendRoom(Client self,String toSend){
        self.getRoom().send(self,toSend);
    }
    
    public boolean send(String Address,String toSend){
        return clientList.get(Address).send(toSend);
    }
    
    public boolean send(Client Cli,String toSend){
        return Cli.send(toSend);
    }
    
    public void removeFromRoom(Client Cli){
        if (Cli.getConnected()){
            Room sRoom = Cli.getRoom();

            if (sRoom != null){
                sRoom.remove(Cli);
            }
        }
    }
    
    public void disconnect(Client Client){
        Room Leaving = Client.getRoom();
        log("Disconnecting client..");
        Client.disconnect();
        clientList.remove(Client.Address);
        removeFromRoom(Client);
        sendRoom(Client, "UDCL " + Client.Address + " " + Client.getName());
        if (Client.Address.equals(Leaving.Creator)){
            sendRoom(Client,"Owner has left.");
            
        }
        
        if (Leaving.Active() == 0 && this.getClient(Leaving.Creator) == null){
            Leaving.shutdown();
            this.nameToNumber.remove(Leaving.getName());
            this.Rooms.remove(Leaving.roomNumber());
        }
    }
}
