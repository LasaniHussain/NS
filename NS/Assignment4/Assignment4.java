import java.util.*;
import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthStyle;


public class Assignment4{
    
    static void init(Client cl1,Client cl2,TimestampingServer ts,PublicKeyServer pks,AuthenticationServer as){
        //symm key between client1 and AS
        DES des = new DES();
        //KeyConversion kc=new KeyConversion();
        SecretKey cl1_AS=des.genDesKey();
        as.addSymmKey(cl1.client_id, cl1_AS);;
        cl1.as_symm_key=cl1_AS;
        //symm key between client2 and AS
        SecretKey cl2_AS=des.genDesKey();
        as.addSymmKey(cl2.client_id, cl2_AS);;
        cl2.as_symm_key=cl2_AS;
        //symm key between PKS and AS
        SecretKey PKS_AS=des.genDesKey();
        as.addSymmKey(pks.id, PKS_AS);
        pks.as_symm_key=PKS_AS;
        //symm key between TS and AS
        SecretKey TS_AS=des.genDesKey();
        as.addSymmKey(ts.id, TS_AS);
        ts.as_symm_key=TS_AS;
        as.DiplaySymmKeys();
        as.pk_id = pks.id;
        as.ts_id = ts.id;
        pks.addPublicKey(ts.id, ts.rsa_public_key);
    }
    public static void main(String[] args){
        //init();
        Client cl1=new Client();
        Client cl2=new Client();
        TimestampingServer ts=new TimestampingServer();
        PublicKeyServer pks=new PublicKeyServer();
        AuthenticationServer as=new AuthenticationServer();
        System.out.println(cl1.client_id+" "+cl2.client_id+" "+ts.id+" "+pks.id+" "+as.id);
        init(cl1,cl2,ts,pks,as);
        KeyConversion kc=new KeyConversion();
        cl1.start();
        //System.out.println(kc.convertDESToString(cl1.as_symm_key)+" "+kc.convertDESToString(cl2.as_symm_key));
        //System.out.println(cl1.doc_hash);
        AuthenticationRequest ar=cl1.send_request_to_as(1);
        DES des=new DES();
        ArrayList<String> res_from_as_enc=as.service_request(ar);
        AuthenticationResponse auth_res = AuthenticationResponse.decAuthenticationResponse(res_from_as_enc, cl1.as_symm_key);
        //creating timestamping request
        ClientTimestampingRequest cl_ts_req=new ClientTimestampingRequest(cl1.client_id,cl1.doc_hash);
        //Extracting K_ct;
        //SecretKey K_ct=KeyConversion.convertToDESKey(des.decryption(res_from_as_enc.get(0),cl1.as_symm_key));
        //encrypting request with Kct
        //System.out.println("hash"+cl1.doc_hash);
        ArrayList<String> cl_ts_req_enc=cl_ts_req.encClientTimestampingRequest(auth_res.symm_key);
        
        //creating timestamping request

        TimestampingRequest t_req=new TimestampingRequest(cl1.client_id,auth_res.enc_auth_ticket,cl_ts_req_enc);
        //Timestamping response recorded in ts_res_enc
        ArrayList<String> ts_res_enc=ts.timestamping_request(t_req);
        //Decrypting first using K_ct
        TimestampingResponse ts_res_decrypted=TimestampingResponse.decTimestampingResponse(ts_res_enc, auth_res.symm_key);
        //Decrypting next using TS public key
        cl1.store_Doc_response(ts_res_decrypted);
        System.out.println("hash in doc response : "+cl1.dr.doc_hash);
        // part 2

        cl2.start();
        AuthenticationRequest ar2=cl2.send_request_to_as(2);
        ArrayList<String> res_from_as_enc2=as.service_request(ar2);
        AuthenticationResponse auth_res2 = AuthenticationResponse.decAuthenticationResponse(res_from_as_enc2, cl2.as_symm_key);
        //creating request for public key of server
        ClientPublicKeyRequest cl_pbkey_req=new ClientPublicKeyRequest(cl2.client_id,ts.id);
        ArrayList<String> cl_pbkey_req_enc=cl_pbkey_req.encClientPublicKeyRequest(auth_res2.symm_key);
        //creating public key request
        PublicKeyRequest pbkey_req=new PublicKeyRequest(cl2.client_id,auth_res2.enc_auth_ticket,cl_pbkey_req_enc);
        //public key response
        ArrayList<String> pbkey_res_enc=pks.public_key_request(pbkey_req);
        //decrypting to get public key
        PublicKeyResponse pbkey_response_dec=PublicKeyResponse.decPublicKeyResponse(pbkey_res_enc, auth_res2.symm_key);

    }
}