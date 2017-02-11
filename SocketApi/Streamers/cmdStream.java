/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Streamers;

import SocketApi.DataReturn;
import SocketApi.Notifiable;
import SysHttp.Executable;

/**
 *
 * @author DeadMansMarch
 * 
 * Implements the base streams "runnable" mode.
 */
public class cmdStream extends baseStream{
    public cmdStream(DataReturn CON,Executable cmd,Notifiable Notifier){
        super(CON,Notifier);
        super.Execute = true;
        super.Exec = cmd;
    }
    
    
}
