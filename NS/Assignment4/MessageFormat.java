import java.util.*;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthStyle;


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
        res.add(des.encryption(KeyConversion.convertDESToString(symm_key), k));
        res.add(des.encryption(client_id, k));
        res.add(des.encryption(server_id, k));
        return res;
    }
    //TODO: add a decryption method 
    static AuthenticationTicket decAuthenticationTicket(ArrayList<String> enc_auth_ticket, SecretKey k){
        DES des  = new DES();
        SecretKey key = KeyConversion.convertToDESKey(des.decryption(enc_auth_ticket.get(0), k));
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
        res.add(des.encryption(KeyConversion.convertDESToString(symm_key), k));
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
        SecretKey new_key = KeyConversion.convertToDESKey(des.decryption(enc_auth_response.get(0), k)); 
        return new AuthenticationResponse(new_key, des.decryption(enc_auth_response.get(1), k), enc_auth_ticket);
    }
}
class ClientTimestampingRequest{
      String id;
      String doc_hash;
    ClientTimestampingRequest(String id, String hash){
        this.id = id;
        doc_hash = hash;
    }
    ArrayList<String> encClientTimestampingRequest(SecretKey k){
        ArrayList<String> res = new ArrayList<String>();
        DES des = new DES();
        res.add(des.encryption(id, k));
        res.add(des.encryption(doc_hash, k));
        return res;
    }
    static ClientTimestampingRequest decClientTimestampingRequest(ArrayList<String> enc_req, SecretKey k){
        DES des = new DES();
        return new ClientTimestampingRequest(des.decryption(enc_req.get(0), k),des.decryption(enc_req.get(1), k));
    }
}
class TimestampingRequest{
     String client_id;
     ArrayList<String> auth_ticket;
     ArrayList<String> client_request;
    TimestampingRequest(String cid,ArrayList<String> at, ArrayList<String> creq ){
        client_id = cid;
        auth_ticket = at;
        client_request = creq;
    }
}
class TimestampingResponse{
      String doc_hash;
      String gmt_timestamp;
      String server_id;
      ArrayList<String> sign;
    TimestampingResponse(String a, String ts,String sid, ArrayList<String> sign){
        doc_hash = a;
        gmt_timestamp = ts;
        server_id = sid;
        this.sign = sign;
    }
    ArrayList<String> encTimestampingResponse(SecretKey k){
        ArrayList<String> res = new ArrayList<String>();
        DES des = new DES();
        res.add(des.encryption(doc_hash, k));
        res.add(des.encryption(gmt_timestamp, k));
        res.add(des.encryption(server_id, k));
        for(int i = 0;i<sign.size();++i)
        res.add(des.encryption(sign.get(i), k));
        return res;
    }
    static TimestampingResponse decTimestampingResponse(ArrayList<String> enc_res,SecretKey k){
        ArrayList<String> sign = new ArrayList<String>();
        DES des = new DES();
        for(int i = 0;i<4;++i)
            sign.add(des.decryption(enc_res.get(i+3), k));
        return new TimestampingResponse(des.decryption(enc_res.get(0), k), des.decryption(enc_res.get(1), k), des.decryption(enc_res.get(2), k), sign);
    }
}
class DigitalSignature{
      String doc_hash;
      String gmt_timestamp;
      String client_id;
      String server_id;
        DigitalSignature(String a,String b,String c,String d){
        doc_hash = a;
        gmt_timestamp = b;
        client_id = c;
        server_id = d;
    }
    ArrayList<String> encDigitalSignature(PrivateKey k){
        ArrayList<String> res  = new ArrayList<String>();
        RSA rsa = new RSA();
        DES des = new DES();
        SecretKey des_key = des.genDesKey();
        /*
        res.add(rsa.encryption(doc_hash, k));
        res.add(rsa.encryption(gmt_timestamp, k));
        res.add(rsa.encryption(client_id, k));
        res.add(rsa.encryption(server_id, k));
        */
        res.add(des.encryption(doc_hash, des_key));
        res.add(des.encryption(doc_hash, des_key));
        res.add(des.encryption(doc_hash, des_key));
        res.add(rsa.encryption(KeyConversion.convertDESToString(des_key), k));
        return res;
    }
    static DigitalSignature decDigitalSignature(ArrayList<String> enc_sign, PublicKey k){
        RSA rsa = new RSA();
        SecretKey key = KeyConversion.convertToDESKey(rsa.decryption(enc_sign.get(3), k));
        DES des = new DES();
        return new DigitalSignature(des.decryption(enc_sign.get(0), key), des.decryption(enc_sign.get(1), key), des.decryption(enc_sign.get(2), key), des.decryption(enc_sign.get(3), key));
    }
}
class ClientPublicKeyRequest{
      String id;
      String server_id;
    ClientPublicKeyRequest(String id, String sid){
        this.id = id;
        server_id = sid;
    }
    ArrayList<String> encClientPublicKeyRequest(SecretKey k){
        ArrayList<String> res = new ArrayList<String>();
        DES des = new DES();
        res.add(des.encryption(id, k));
        res.add(des.encryption(server_id, k));
        return res;
    }
    static ClientPublicKeyRequest decClientPublicKeyRequestlientTimestampingRequest(ArrayList<String> enc_req, SecretKey k){
        DES des = new DES();
        return new ClientPublicKeyRequest(des.decryption(enc_req.get(0), k),des.decryption(enc_req.get(1), k));
    }
}
class PublicKeyRequest{
      String client_id;
      ArrayList<String> auth_ticket;
      ArrayList<String> client_request;
    PublicKeyRequest(String cid,ArrayList<String> at, ArrayList<String> creq ){
        client_id = cid;
        auth_ticket = at;
        client_request = creq;
    }
}
class PublicKeyResponse{
      PublicKey pub_key;
    PublicKeyResponse(PublicKey k){
        pub_key = k;
    }
    ArrayList<String> encPublicKeyResponse(SecretKey k){
        ArrayList<String> res = new ArrayList<String>();
        DES des = new DES();
        res.add(des.encryption(KeyConversion.convertRSAtoString(pub_key), k));
        return res;
    }
    static PublicKeyResponse decPublicKeyResponse(ArrayList<String> enc_res,SecretKey k){
        DES des = new DES();
        return new PublicKeyResponse(KeyConversion.convertToRSAKey(des.decryption(enc_res.get(0), k)));
    }
}
class DocResponse{
    String doc; //TODO:see if this needs to be changed
    String gmt_timestamp;
    String server_id;
    ArrayList<String> sign;
    DocResponse(String d, String ts,String sid,ArrayList<String> sign){
        doc = d;
        gmt_timestamp = ts;
        server_id = sid;
        this.sign = sign;
    }
}
