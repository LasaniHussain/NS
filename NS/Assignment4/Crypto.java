import java.util.*;
import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthStyle;

class DES{
    SecretKey genDesKey(){
        SecretKey key=null;
        try {
            // generate secret key using DES algorithm
            key = KeyGenerator.getInstance("DES").generateKey();
            //return key;
        }
        catch(Exception e)
        {
        }
        return key;
    }

    String encryption(String plaintext, SecretKey key){
        try{
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return encrypt(plaintext, cipher);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String encrypt(String str, Cipher cipher) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = cipher.doFinal(utf8);
            return Base64.getEncoder().encodeToString(enc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
       
    private String decrypt(String str, Cipher cipher) {
        try {
            byte[] dec = Base64.getDecoder().decode(str.getBytes());
            byte[] utf8 = cipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String decryption(String ciphertext, SecretKey key){
        try{
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return decrypt(ciphertext, cipher);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
class RSA{//TODO:check this functionality

    KeyPair genRSAKeys(){
        KeyPair kpg = null;
        try {
            KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
            k.initialize(2048);
            kpg = k.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kpg;
    }

    String encryption(String plaintext,Key key){
        
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] secretMessageBytes = plaintext.getBytes("UTF8");
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    String decryption(String ciphertext, Key key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] secretMessageBytes = ciphertext.getBytes("UTF8");
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    String sign(String str,Key key){
        return "";
    }
    boolean verify_sign(){
        return false;
    }
}
