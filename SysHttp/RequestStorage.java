/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SysHttp;

import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class RequestStorage {
    HashMap<Integer,Request> Requests = new HashMap<>();
    
    public void storeRequest(int Id, String Dat){
        if (Requests.containsKey(Id)){
            Request k = Requests.get(Id);
            k.Append(Dat);
        }else{
            Requests.put(Id, new Request(Dat));
        }
    }
    
    public Request getRequest(int Id){
        Request k = Requests.get(Id).Copy();
        Requests.remove(Id);
        return k;
    }
}
