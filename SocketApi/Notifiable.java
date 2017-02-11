/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

/**
 *
 * @author DeadMansMarch
 */
public interface Notifiable {
    public boolean isDisconnected();
    public void Disconnect();
    public void Finish(DataReturn K,boolean fin);
    public boolean isFinished();
    public void NotifyListeners();
    public String[] getNewStreams();
    public void StreamStart(String Addr);
}
