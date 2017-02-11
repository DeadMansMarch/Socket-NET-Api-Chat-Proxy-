/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author DeadMansMarch
 */
public class Crypt {
    private SecretKey Key;
    private Cipher AES;
    
    public Crypt(SecretKey Key){
        this.Key = Key;
        try{
            AES =  Cipher.getInstance("AES/ECB/PKCS5Padding");
        }catch(NoSuchAlgorithmException | NoSuchPaddingException E){
            
        }
    }
    
    public String Encrypt(String plaintext){
        String En = encryptAES(plaintext,this.Key);
        return En;
    }
    
    public String Decrypt(String cipher){
        String De = decryptAES(cipher,this.Key);
        return De;
    }
    
    
    private String decryptAES(String Plain,SecretKey Key){
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(Plain);
            AES.init(Cipher.DECRYPT_MODE, Key);
            return new String(AES.doFinal(encryptedTextByte));
        }catch(Exception E){
            System.out.println(E + "DE");
        }
        return "ERROR";
    }
    
    private String encryptAES(String Plain,SecretKey Key){
        try{
            byte[] plainTextByte = Plain.getBytes();
            AES.init(Cipher.ENCRYPT_MODE, Key);
            byte[] encryptedByte = AES.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedByte);
        }catch(Exception E){
            System.out.println(E + "EN");
        }
        return "ERROR";
    }
}
