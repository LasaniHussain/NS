import java.util.*;
import java.time.*;
import java.text.*;
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
        //System.out.println("inside service request\n");
        /*if(!symm_keys.containsKey(id))
            return null;*/
        DES des = new DES();
        SecretKey new_key = des.genDesKey();
        AuthenticationTicket auth_ticket;
        AuthenticationResponse  response;
        switch(auth_req.service_type){
            case 1: 
                auth_ticket = new AuthenticationTicket(new_key, auth_req.id, ts_id); 
                ArrayList<String> enc_ticket = auth_ticket.encryptAuthenticationTicket(symm_keys.get(ts_id));
                //System.out.println(enc_ticket.size());
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
    SecretKey as_symm_key;
    private Hashtable<String,PublicKey> public_keys = new Hashtable<String,PublicKey>();
    PublicKeyServer()
    {
        Random rn = new Random();
        id= String.valueOf(200+rn.nextInt(100));
    }
    void init_as_symm_key(SecretKey k){
        as_symm_key = k;
    }
    void addPublicKey(String id,PublicKey public_key){
        public_keys.put(id, public_key);
    }
    ArrayList<String> public_key_request(PublicKeyRequest pk_req){
        /*
        1. decrypt the ticket from as 
        2. verify if the id for the server matches its own
        3. verfify the id of the client with the one in the ticket
        4. decrypt the request from the client
        5. find public key requires
        6. send the encrypted response back
        */
        AuthenticationTicket auth_ticket = AuthenticationTicket.decAuthenticationTicket(pk_req.auth_ticket, as_symm_key);
        if(auth_ticket.server_id.equals(id)){
            if(pk_req.client_id.equals(auth_ticket.client_id)){
                ClientPublicKeyRequest c_req = ClientPublicKeyRequest.decClientPublicKeyRequestlientTimestampingRequest(pk_req.client_request, auth_ticket.symm_key);
                PublicKey k = public_keys.get(c_req.server_id);//we will assume that the key is always found
                //System.out.println("Server id : "+c_req.server_id+"Public key : "+k);
                PublicKeyResponse res = new PublicKeyResponse(k);
                System.out.println("returninng public key response");
                return res.encPublicKeyResponse(auth_ticket.symm_key);
            }
        }
        return null;
    }
}

class TimestampingServer{
    String id;
    SecretKey as_symm_key;
    PublicKey rsa_public_key;//maybe not needed here
    private PrivateKey rsa_private_key;
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
    void init_as_symm_key(SecretKey k){
        as_symm_key = k;
    }
    ArrayList<String> timestamping_request(TimestampingRequest t_req){
        /*
        1. decrypt the ticket from as 
        2. verify if the id for the server matches its own
        3. verfify the id of the client with the one in the ticket
        4. decrypt the request from the client
        5. generate the timestamp
        6. sign the hash with certain fields
        7. send the encrypted response back
        */
        AuthenticationTicket auth_ticket = AuthenticationTicket.decAuthenticationTicket(t_req.auth_ticket, as_symm_key);
        if(auth_ticket.server_id.equals(id)){
            if(t_req.client_id.equals(auth_ticket.client_id)){
                ClientTimestampingRequest c_req = ClientTimestampingRequest.decClientTimestampingRequest(t_req.client_request, auth_ticket.symm_key);
                final Date currentTime = new Date();
                final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String ts = sdf.format(currentTime);
                DigitalSignature ds = new DigitalSignature(c_req.doc_hash, ts, t_req.client_id, id);
                ArrayList<String> sign = ds.encDigitalSignature(rsa_private_key);
                TimestampingResponse t_res = new TimestampingResponse(c_req.doc_hash, ts, id, sign);
                System.out.println("generating timestamping response");
                return t_res.encTimestampingResponse(auth_ticket.symm_key);
            }
        }
        return null;
    }
}

