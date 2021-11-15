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
    ArrayList<String> enc_list(ArrayList<String> str_list,SecretKey key)
    {
        ArrayList<String> sol=new ArrayList<String>();
        for(int i=0;i<str_list.size();i++)
        {
            sol.add(encryption(str_list.get(i), key));
        }
        return sol;
    }
    ArrayList<String> dec_list(ArrayList<String> str_list,SecretKey key)
    {
        ArrayList<String> sol=new ArrayList<String>();
        for(int i=0;i<str_list.size();i++)
        {
            sol.add(decryption(str_list.get(i), key));
        }
        return sol;
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
            k.initialize(4096);
            kpg = k.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kpg;
    }

    String encryption(String plaintext,PrivateKey key){
        
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] secretMessageBytes = plaintext.getBytes("UTF8");
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            System.out.println("enc bytes"+encryptedMessageBytes.length);
            //System.out.println();
            String res = Base64.getEncoder().encodeToString(encryptedMessageBytes);
            //String res = new String(encryptedMessageBytes);
            System.out.println("enc string "+res.length());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    String decryption(String ciphertext, PublicKey key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] secretMessageBytes = Base64.getDecoder().decode(ciphertext.getBytes());
            System.out.println(secretMessageBytes.length);
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return new String(encryptedMessageBytes,"UTF-8");
            //return new String(encryptedMessageBytes,"UTF-8");
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
