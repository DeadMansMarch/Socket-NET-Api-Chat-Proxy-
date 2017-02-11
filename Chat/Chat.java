/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import SocketApi.IAddress;
import static java.lang.Thread.sleep;
import java.util.Scanner;

/**
 *
 * @author DeadMansMarch
 */
public class Chat {
    public static void main(String[] args){
        //Diffie Hellman test.
        /*
        chatServer Server = new chatServer(new IAddress("192.168.0.102",8888));
        
        chatClient Client = new chatClient();
        Client.connect(new IAddress("192.168.0.102",8888), 0);
        */
        
        Chat m = new Chat();
        m.connectChat(new String[]{"100.1.215.220","8889"});
        //m.runServer(new String[]{"192.168.1.164","8889"});
    }
    
    private void runServer(String[] Dat){
        chatServer Server = new chatServer(new IAddress(Dat[0],Integer.parseInt(Dat[1]))); //Runs itself.
        System.out.println("CONNECTED");
        for (int i = 0;i<1000;i++){
            if (!Server.testDiffieHellman()){
                System.out.println("Error in diffie hellman");
                break;
            }
        }
        System.out.println("Diffie Hellman nominal.");
    }
    
    public void connectChat(String[] Dat){
        chatClient Client1 = new chatClient();
        System.out.println(new IAddress(Dat[0],Integer.parseInt(Dat[1])));
        Client1.connect(new IAddress(Dat[0],Integer.parseInt(Dat[1])),"Cancer");
    }
    
    public void openChat(String[] Dat){
        runServer(Dat);
    }
    
    public void runChat(String[] args){
        Scanner K = new Scanner(System.in);
        
        
        
        if (args.length > 0){
            String SERV = (args.length <= 0) ? K.nextLine() : args[0];
            try{
                String[] Dat = SERV.split(":");
                
                
                
                //chatClient Client1 = new chatClient();
                //Client1.connect(new IAddress(Dat[0],Integer.parseInt(Dat[1])),0);

            }catch(Exception E){
                System.out.println("Error. " + E);
            }
            
        }else{
            System.out.print("Connect to : ");
            String[] CON = K.nextLine().split(":");
            
            
        }
    }
}
