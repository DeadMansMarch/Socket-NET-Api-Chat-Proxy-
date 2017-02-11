/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Streamers;

import SocketApi.DataReturn;
import SocketApi.Notifiable;
import SysHttp.Executable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimerTask;

/**
 *
 * @author DeadMansMarch
 */
public class baseStream extends TimerTask{
        protected DataReturn CON;
        protected InputStream INP;
        protected BufferedReader BUFF;
        protected InputStreamReader INPR;
        protected final Notifiable Notifier;
        protected boolean SendListener = false;
        protected boolean Execute = false;
        protected Executable Exec;
        
        
        
        public baseStream(DataReturn CON,Notifiable Notifier){
            this.CON = CON;
            
            this.Notifier = Notifier;
            Notifier.StreamStart(this.CON.getAddress().toString());
            try{
                setupLocal();
            }catch(IOException E){
                
            }
        }
        
        protected void setupLocal() throws IOException{
            this.INP = this.CON.getStream().getInputStream();
            this.INPR = new InputStreamReader(this.INP);
            this.BUFF = new BufferedReader(INPR);
            this.SendListener = CON.getPClass().equals("class SocketApi.Sender");
        }
        
        protected void UPD(){
            this.CON.getNotifier().NotifyListeners();
        }
        
        protected void Finish(boolean is){
            this.CON.getNotifier().Finish(CON, is);
        }


        public boolean isSendListener(){
            return this.SendListener;
        }
        
        
        @Override
        public void run(){
            try{
                if (true){
                    boolean EndTimer = false;
                    if (CON.getStream().isClosed()){
                        EndTimer = true;
                    }
                    String read = BUFF.readLine();
                    if (EndTimer){
                        CON.getTimer().cancel();
                        CON.Remove();
                        this.UPD();
                        return;
                    }
                    if (read != null){
                        if (Execute){
                            this.Exec.run(CON.getAddress().toString(),read);
                        }
                        CON.Streamed(read);
                        this.UPD();
                    }

                
                }
                
            }catch(IOException E){
                CON.getTimer().cancel();
                CON.Remove();
                this.Notifier.Disconnect();
                System.out.println("Timer Error [id = " + this.CON.getLocalId() + "] :" + E);
            }
        }
    }
