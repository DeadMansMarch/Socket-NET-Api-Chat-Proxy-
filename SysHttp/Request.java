/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SysHttp;

import SocketApi.IAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author DeadMansMarch
 */
public class Request {
    ArrayList<String> Request = new ArrayList<>(2000);
    int Host = -1;
    public Request(){
        
    }
    
    public Request(String Initial){
        Request = new ArrayList<>(Arrays.asList(Initial.split("\n")));
    }
    
    public synchronized void Append(String Next){
        int Size = Request.size();
        Request.add(Next);
        if (Host < 0 && Next.contains("Host:")){
            this.Host = Request.indexOf(Next);
        }
    }
    
    public IAddress getHost(){
        if (Request.size() > 0 && Host >= 0){
            String HostL = Request.get(Host);
            String HostS = "";
            int HostIndex = 0;
            String Port = "";
            for (int i = 5;i<HostL.length();i++){
                char k = HostL.charAt(i);
                if (k != ' ' && k != '\n'){
                   
                    if (k == ':'){
                        HostIndex = i;
                    }else if (HostIndex > 0){
                        Port += k;
                    }else{
                        HostS += k;
                    }
                }
            }
            return new IAddress(HostS,(!Port.equals("")) ? Integer.parseInt(Port) : 80);
        }
        return new IAddress("",-1);
    }
    
    public String[] getLines(){
        
        List<String> Lines = this.Request.stream().map( i -> (i + "\n")).collect(Collectors.toList());
        return Lines.toArray(new String[Lines.size()]);
    }
    
    public Request Copy(){
        Request New = new Request();
        New.Host = this.Host;
        New.Request = (ArrayList<String>) this.Request.clone();
        return New;
    }
    
    @Override
    public String toString(){
        String R = "";
        R = Request.stream().map((Line) -> "\n" + Line).reduce(R, String::concat);
        return R;
    }
    
}
