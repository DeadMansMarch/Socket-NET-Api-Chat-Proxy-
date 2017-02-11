/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public interface DataReturn {
    public Socket getStream();
    public void Remove();
    public boolean Respond(String Data);
    public boolean Respond(String[] Data);
    public IAddress getAddress();
    public void Streamed(String STRM);
    public Timer getTimer();
    public String[] getNewRequests();
    public String getPClass();
    public int getLocalId();
    public Notifiable getNotifier();
}
