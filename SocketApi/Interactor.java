/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.io.IOException;
import java.util.Timer;

/**
 *
 * @author DeadMansMarch
 */
public interface Interactor {
    public void removeStreamer(String Interactor);
    public Timer getTimer(String Timer);
    public DataReturn getStreamer(String K);
    public void listen(Notifiable Notifier) throws Exception;
}
