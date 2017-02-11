/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SocketApi.IAddress;
import SocketApi.Notifiable;
import SocketApi.Notifier;
import SocketApi.cmdSender;
import SysHttp.Executable;
import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.SecretKey;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author DeadMansMarch
 * 
 * For use as main class, will run client.
 */
public class chatClient {
    protected final String Version = "BETA0.2";
    protected final UI K = new UI(this);
    protected final Notifiable Blank = new Notifier();
    protected final Random random = new Random();
    protected final Encryptor CLI = new Encryptor("CLIEncryptor");
    protected final SoundEngine PING = new SoundEngine();
    protected final HashMap<String,String> nameLookup = new HashMap<>();
    protected cmdSender Connection;
    protected String Room = "";
    protected boolean rpA = false;
    protected boolean Connected = false;
    protected String rpaLine = "";
    protected String ServerVers = "";
    protected int KeysSet = 0;
    
    void connAttempt(String Address,String Data){
        String[] Split = Data.split(" ");
        switch(Split[0].toLowerCase()){
            case "ver":
                send("VER " + this.Version);
                log("Version sent.");
                break;
            case "upg":
                log("You are using an outdated version.");
                break;
            case "encrypt-length":
                
                String Key = Split[1];
                SecretKey Keys = Encryptor.stringToKey(Key);
                CLI.addLayer(Keys);
                this.K.addMessage("[Local]", "Key one completed and in use.");
                send("enumv");
               
                break;
            case "evars":
                System.out.println("ERROR: envar unsecured.");
                break;
            case "bvars":
                System.out.println("ERROR: envar unsecured.");
                break;
            default:
                if (CLI.ready()){
                    
                    String[] FullData = CLI.Decrypt(Data).split(" ");
                    switch(FullData[0]){
                        case "sp":
                            this.K.addMessage("[Server]", "Please input a room password. Blank passwords will be accepted, but less secure.");
                            this.rpA = true;
                            this.rpaLine = "sp";
                            break;
                        case "rp":
                            this.K.addMessage("[Server]", "This room is password protected. Please input password.");
                            this.rpA = true;
                            this.rpaLine = "rp";
                            break;
                        case "OWNW":
                            this.K.addMessage("[Server]", "Server waiting on owner to intialize room settings.");
                            break;
                        case "CONNECTED":
                            Connected = true;
                            this.ServerVers= FullData[1];
                            this.Room = FullData[2];
                            this.K.addName("self", "You (No Nickname)");
                            log("Connected to server.");
                            log("This server is running V" + this.ServerVers);
                            send("/UDUL");
                            break;
                        case "evars":
                            String PRI2 = Integer.toString(random.nextInt(4000));
                            CLI.setVar("g", FullData[1]);
                            CLI.setVar("p",FullData[2]);
                            CLI.setVar("PRI", PRI2);
                            
                            send("bvars " + CLI.passFunc(CLI.getVars(new String[]{"g","p","PRI"})));
                            break;
                        case "bvars":
                            CLI.setVar("ENt2",FullData[1]);
                            int New = CLI.privatize(CLI.passFunc(CLI.getVars(new String[]{"ENt2","p","PRI"})));
                            if (New == 1){
                                KeysSet += 1;
                                this.K.addMessage("[Local]", "Key " + (KeysSet + 1) + " completed and in use.");
                            }
                            if (!CLI.ready(10)){
                                send("enumv");
                            }else{
                                send("attempt");
                            }
                            break;
                        
                        default:
                            break;
                    }
                }else{
                    //Probably LI - VE connection tick.
                }
                break;
        }
    }
    
    public void Sound(int Freq,int Time){
        try{
            PING.makeSound(Freq,Time);
        }catch(LineUnavailableException E){
            
        }
    }
    
    void connRead(String Address, String Data){
        String TrueData = CLI.Decrypt(Data);
        String[] Split = TrueData.split(" ");
        switch(Split[0]){
            case "message":
                this.K.addMessage("[" + nameLookup.get(Split[1]) + "]", CatDrop(Split,2));
                
                Sound(500,100);
                break;
            case "announce":
                this.K.addMessage("ANNOUNCE", CatDrop(Split,1));
                break;
            case "UDCL":
                this.K.removeName(Split[1]);
                this.K.addMessage("[Server]", nameLookup.get(Split[1]) + " has left the server.");
                nameLookup.remove(Split[1]);
                break;
            case "UDCA":
                this.K.addName(Split[1],Split[2]);
                this.K.addMessage("[Server]", "[" + Split[2] + "] has joined the room.");
                nameLookup.put(Split[1], Split[2]);
                break;
            case "UDCN":
                String Cat = CatDrop(Split,2);
                this.K.changeName(Split[1], Cat);
                this.K.addMessage("[Server]", nameLookup.get(Split[1]) + " has changed their name to " + Cat + ".");
                nameLookup.put(Split[1], Cat);
                break;
            case "CU":
                for (int i = 1;i<Split.length;i++){
                    String[] Total = Split[i].split(">");
                    nameLookup.put(Total[0], Total[1]);
                    this.K.addName(Total[0], Total[1]);
                }
                break;
            default:
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
    
    boolean Messaging = true;
    
    private void stinReader(){
        Console New;
        Scanner SCN;
        New = System.console();
        SCN = new Scanner(System.in);
        
        Thread STIN = new Thread(()->{
            while (Messaging){
                String k;
                if (New != null){
                    k = New.readLine();
                }else{
                    k = SCN.nextLine();
                }
                if (!k.equals("")){
                    send(k);
                }
            }
        });
        
        STIN.start();
    }
    
    public void cutMessages(){
        this.Messaging = false;
    }
    
    public void startMessages(){
        stinReader();
    }
    
    private class Executer extends Executable{
        chatClient Reference;
        public Executer(chatClient Ref){
            this.Reference = Ref;
        }
        
        @Override
        public void run(String Address,String Data){
            if (!Connected){
                connAttempt(Address,Data);
            }else{
                connRead(Address,Data);
                
            }
        }
    }
    
    
    public chatClient(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                this.Connection.getStreamer("").getStream().close();
            }catch(Exception E){
                
            }
        }));
    }
    
    public void log(String E){
        System.out.println("[CLI] " + E);
    }
    
    public void connect(IAddress ADDR,String Roomname){
        
        Connection = new cmdSender(ADDR,new Executer(this));
        try{
            Connection.listen(Blank);
        }catch(Exception E){
            
        }
        send("Connect /" + Roomname + "/ TCP1.1");
        log("Sent data.");
        
    }

    
    public void send(String Data){
        if (this.rpA){
            log("Sending RPA special with tag : " +this.rpaLine);
            this.Connection.send(CLI.Encrypt(this.rpaLine + " " + Data));
            this.rpA = false;
            return;
        }
        
        if (CLI.ready()){
            if (Data.charAt(0) != '/'){
                if (this.Connected){
                    this.Connection.send(CLI.Encrypt("message " + Data));
                }else{
                    this.Connection.send(CLI.Encrypt(Data));
                }
            }else{
                String[] Splt = Data.split(" ");
                switch(Splt[0]){    //local action
                    case "/name":
                        this.K.changeName("self", "You (" + CatDrop(Splt,1) + ")");
                        break;
                    default:
                        break;
                }
                this.Connection.send(CLI.Encrypt(Data.substring(1)));
                
            }
        }else{
            this.Connection.send(Data);
        }
    }
}
