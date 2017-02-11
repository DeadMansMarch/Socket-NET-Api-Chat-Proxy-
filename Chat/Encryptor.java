/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author DeadMansMarch
 */
public class Encryptor {
    private int Ready = 0; // Ready by encryption layer.
    private KeyGenerator Keys;
    private HashMap<String,String> Vars = new HashMap<>();
    private String privatized = "";
    private ArrayList<Crypt> Layers = new ArrayList<>();
    private String logTag = "";
    
    private void log(String Text){
        System.out.println("[" + logTag + "] " + Text);
    }
    public SecretKey generateSecret(){
        return Keys.generateKey();
    }
    
    public SecretKey makeSecret(int k){
        return null;
    }
    
    public int Using(){
        return this.Layers.size();
    }
    
    public Encryptor(String Type){
        this.logTag = Type;
        try{
            this.Keys = KeyGenerator.getInstance("AES");
            Keys.init(128);
        }catch(Exception E){
            
        }
    }
   
    public String passFunc(String[] k){
        return new BigInteger(k[0]).modPow(new BigInteger(k[2]),new BigInteger(k[1])).toString();
    }
    
    public void setVar(String Key,String Val){
        this.Vars.put(Key,Val);
    }
    
    public String getVar(String Key){
        return this.Vars.get(Key);
    }
    
    public String[] getVars(String[] Keys){
        String[] vals = new String[Keys.length];
        for (int i = 0;i<Keys.length;i++){
            vals[i] = getVar(Keys[i]);
        }
        return vals;
    }
    
    public int privatize(String Priv){
        if (privatized.length() + Priv.length() >= 16){
            privatized += Priv;
            this.addLayer((Encryptor.byteToKey(hashReduce(privatized))));
            this.privatized = "";
            return 1;
        }else{
            privatized += Priv;
            return 0;
        }
    }
    
    public void addLayer(SecretKey New){
        this.Layers.add(new Crypt(New));
    }
    
    public static String keyToString(SecretKey Key){
        return Base64.getEncoder().encodeToString(Key.getEncoded());
    }
    
    public static byte[] hash(String unHashed){
        try{
            byte[] partial = (unHashed).getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            return sha.digest(partial);
        }catch(NoSuchAlgorithmException E){
            
        }
        return null;
    }
    
    public static String hashToString(byte[] hash){
        return new String(hash, StandardCharsets.UTF_8);
    }
    
    public static byte[] hashReduce(String unHashed){
        return Arrays.copyOf(hash(unHashed), 16);
    }
    
    public static SecretKey stringToKey(String Key){
        byte[] Decoded = Base64.getDecoder().decode(Key);
        return new SecretKeySpec(Decoded, 0, Decoded.length, "AES");
    }
    
    public static SecretKey byteToKey(byte[] Key){
        return new SecretKeySpec(Key, "AES");
    }
    
    public boolean ready(){
        return ready(1);
    }
    
    public boolean ready(int level){
        return this.Layers.size() >= level;
    }
    
    public String Encrypt(String plain){
        String Crpt = plain;
        for (Crypt K : Layers){
            Crpt = K.Encrypt(Crpt);
        }
        return Crpt;
    }
    
    public String Decrypt(String Cipher){
        String Crpt = Cipher;
        for (int i = Layers.size() - 1; i>=0 ;i--){
            Crypt K = Layers.get(i);
            Crpt = K.Decrypt(Crpt);
        }
        return Crpt;
    }
}
