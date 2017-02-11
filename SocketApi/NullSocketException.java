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
class NullSocketException extends Exception {

    public NullSocketException() {
        
    }
    
    @Override
    public String getMessage(){
        return "Socket connection void.";
    }
    
}
