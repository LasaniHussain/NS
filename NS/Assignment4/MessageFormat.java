import java.util.*;
import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthStyle;
import javax.crypto.spec.SecretKeySpec;

class SecretKeyConversion{
    static String convertToString(SecretKey k){
        return Base64.getEncoder().encodeToString(k.getEncoded());
    }
    static SecretKey convertToSecretKey(String k){
        byte[] decodedKey = Base64.getDecoder().decode(k);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES"); 
    }
}

class AuthenticationRequest{
    String id;
    int service_type;
    AuthenticationRequest(String id, int service_type){
        this.id = id;
        this.service_type  = service_type;
    }
}

class AuthenticationTicket{
    SecretKey symm_key;
    String client_id;
    String server_id;
    AuthenticationTicket(SecretKey k, String cid, String sid){
        symm_key = k;
        client_id = cid;
        server_id = sid;
    }
    ArrayList<String> encryptAuthenticationTicket(SecretKey k){
        DES des = new DES();
        ArrayList<String> res = new ArrayList<String>();
        res.add(des.encryption(SecretKeyConversion.convertToString(symm_key), k));
        res.add(des.encryption(client_id, k));
        res.add(des.encryption(server_id, k));
        return res;
    }
    //TODO: add a decryption method 
    static AuthenticationTicket decAuthenticationTicket(ArrayList<String> enc_auth_ticket, SecretKey k){
        DES des  = new DES();
        SecretKey key = SecretKeyConversion.convertToSecretKey(des.decryption(enc_auth_ticket.get(0), k));
        return new AuthenticationTicket(key, des.decryption(enc_auth_ticket.get(1), k),des.decryption(enc_auth_ticket.get(2), k));
    }
}

class AuthenticationResponse{
    SecretKey symm_key;
    String server_id;
    ArrayList<String> enc_auth_ticket;
    AuthenticationResponse(SecretKey k, String sid,ArrayList<String> enc_ticket){
        symm_key = k;
        server_id = sid;
        enc_auth_ticket = enc_ticket;
    }
    ArrayList<String> encAuthenticationResponse(SecretKey k){
        DES des = new DES();
        ArrayList<String> res = new ArrayList<String>();
        res.add(des.encryption(SecretKeyConversion.convertToString(symm_key), k));
        res.add(des.encryption(server_id, k));
        for(int i = 0;i<3;++i)
            res.add(des.encryption(enc_auth_ticket.get(i), k));
        return res;

    }
    static AuthenticationResponse decAuthenticationResponse(ArrayList<String> enc_auth_response, SecretKey k){
        DES des = new DES();
        ArrayList<String> enc_auth_ticket = new ArrayList<String>();
        for(int i = 0;i<3;++i)
            enc_auth_ticket.add(des.decryption(enc_auth_response.get(i+2), k));
        SecretKey new_key = SecretKeyConversion.convertToSecretKey(des.decryption(enc_auth_response.get(0), k)); 
        return new AuthenticationResponse(new_key, des.decryption(enc_auth_response.get(1), k), enc_auth_ticket);
    }
}

class TimestampingRequest{

}

class PublicKeyRequest{

}

