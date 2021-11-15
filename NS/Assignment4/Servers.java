import java.util.*;
import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthStyle;

class AuthenticationServer{
    private Hashtable<String,SecretKey> symm_keys = new Hashtable<String,SecretKey>();
    String id;
    String ts_id;
    String pk_id;
    AuthenticationServer()
    {
        Random rn = new Random();
        id= String.valueOf(300+rn.nextInt(100));
        
    }
    void addSymmKey(String id,SecretKey symm_key){
        symm_keys.put(id,symm_key);
    }
    void DiplaySymmKeys()
    {   System.out.println(symm_keys);
        /*for(int i=0;i<symm_keys.size();i++)
        System.out.println(symm_keys.);*/
    }
    String service_request(String client_id, int serviceType){
        if(!symm_keys.containsKey(id))
            return "";
        DES des = new DES();
        SecretKey new_key = des.genDesKey();
        String ticket;
        String  response;
        switch(serviceType){
            case 1: 
                ticket = new_key.toString()+client_id+ts_id;
                ticket = des.encryption(ticket, symm_keys.get(ts_id));
                response = new_key.toString()+ts_id+ticket;
                return des.encryption(response, symm_keys.get(client_id));
            case 2: 
                ticket = new_key.toString()+client_id+pk_id;
                ticket = des.encryption(ticket, symm_keys.get(pk_id));
                response = new_key.toString()+pk_id+ticket;
                return des.encryption(response, symm_keys.get(client_id));
            default: 
                return "";
        }
    }
}

class PublicKeyServer{//changing name for CA
    String id;
    String as_symm_key;
    private Hashtable<String,String> public_keys = new Hashtable<String,String>();
    PublicKeyServer()
    {
        Random rn = new Random();
        id= String.valueOf(200+rn.nextInt(100));
    }
    void addPublicKey(String id,String public_key){

    }
    String public_key_request(String id, String ticket,String request){
        return "";
    }
}

class TimestampingServer{
    String id;
    String as_symm_key;
    Key rsa_public_key;//maybe not needed here
    private Key rsa_private_key;
    void addPrivateKey(String private_key){}
    TimestampingServer()
    {
        Random rn = new Random();
        id= String.valueOf(100+rn.nextInt(100));
        RSA rsa = new RSA();
        KeyPair kpg = rsa.genRSAKeys();
        rsa_public_key = kpg.getPublic();
        rsa_private_key = kpg.getPrivate();
    }

    String timestamping_request(String id, String ticket,String request){
        return "";
    }
}

