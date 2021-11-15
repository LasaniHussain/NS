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
    ArrayList<String> service_request(AuthenticationRequest auth_req){
        if(!symm_keys.containsKey(id))
            return null;
        DES des = new DES();
        SecretKey new_key = des.genDesKey();
        AuthenticationTicket auth_ticket;
        AuthenticationResponse  response;
        switch(auth_req.service_type){
            case 1: 
                auth_ticket = new AuthenticationTicket(new_key, auth_req.id, ts_id); 
                ArrayList<String> enc_ticket = auth_ticket.encryptAuthenticationTicket(symm_keys.get(ts_id));
                response = new AuthenticationResponse(new_key,ts_id,enc_ticket);
                return response.encAuthenticationResponse(symm_keys.get(auth_req.id));
            case 2: 
                auth_ticket = new AuthenticationTicket(new_key, auth_req.id, pk_id); 
                enc_ticket = auth_ticket.encryptAuthenticationTicket(symm_keys.get(pk_id));
                response = new AuthenticationResponse(new_key,pk_id,enc_ticket);
                return response.encAuthenticationResponse(symm_keys.get(auth_req.id));
            default: 
                return null;
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

