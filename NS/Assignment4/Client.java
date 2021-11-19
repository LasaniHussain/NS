import java.util.*;
import java.io.*;
import java.math.*;
import java.security.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.util.*; 
class Client{
    SecretKey as_symm_key;
    String client_id;
    //String timestamped_doc;//this can either by the doc timestamped by ts or recieved form another client
    DocResponse dr;
    String doc_hash;
    String doc_path;
    Client()
    {   Random rn = new Random();
        client_id= String.valueOf(rn.nextInt(100));
    }
    void genHash()
    {
        
        try
        {
            File file = new File(doc_path);
            PDDocument document = PDDocument.load(file);
            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();
            //Retrieving text from PDF document
            String text = pdfStripper.getText(document);
            //System.out.println(text);
            MessageDigest md5;
            try 
            {
                md5 = MessageDigest.getInstance("MD5");
            } 
            catch (NoSuchAlgorithmException e) 
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            md5.update(text.getBytes());
            byte[] digest = md5.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            doc_hash = bigInt.toString(16);
            System.out.println("hash of doc gen by client"+doc_hash);
            //return hashtext;
        }
        catch(Exception e)
        {
            System.out.print(e);
        }
    }
    void verify_timestamp(Client cl1,PublicKeyServer pks,TimestampingServer ts,AuthenticationServer as)
    {   System.out.println("\nVerifying timestamp");
        request_doc(cl1);
        //System.out.println("recieved from doc response : "+dr.doc_hash+"\n"+dr.gmt_timestamp);
        AuthenticationRequest ar=send_request_to_as(2);
        System.out.println("\nRequest sent to Authentication Server : < "+ar.id+" || "+ar.service_type+" >");
        ArrayList<String> res_from_as_enc=as.service_request(ar);
        AuthenticationResponse auth_res = AuthenticationResponse.decAuthenticationResponse(res_from_as_enc, as_symm_key);
        System.out.println("\nDecrypted response from Authentication Server : \n< "+auth_res.symm_key+"  ||  "+auth_res.server_id+"  ||  "+auth_res.enc_auth_ticket.get(0)+" || "+auth_res.enc_auth_ticket.get(1)+" || "+auth_res.enc_auth_ticket.get(2)+" >");
        ClientPublicKeyRequest cl_pbkey_req=new ClientPublicKeyRequest(client_id,ts.id);
        System.out.println("\nRequest framed at client :  < "+cl_pbkey_req.id+" || "+cl_pbkey_req.server_id+" >");
        ArrayList<String> cl_pbkey_req_enc=cl_pbkey_req.encClientPublicKeyRequest(auth_res.symm_key);
        PublicKeyRequest pbkey_req=new PublicKeyRequest(client_id,auth_res.enc_auth_ticket,cl_pbkey_req_enc);
        System.out.println("\nPublic key request sent to Public Key Server :\n< "+pbkey_req.client_id+" || "+pbkey_req.auth_ticket.get(0)+" || "+pbkey_req.auth_ticket.get(1)+" || "+pbkey_req.auth_ticket.get(2)+" || "+pbkey_req.client_request.get(0)+" || "+pbkey_req.client_request.get(1)+" >");
        ArrayList<String> pbkey_res_enc=pks.public_key_request(pbkey_req);
        PublicKeyResponse pbkey_response_dec=PublicKeyResponse.decPublicKeyResponse(pbkey_res_enc, auth_res.symm_key);
        DigitalSignature sign_from_cl1=DigitalSignature.decDigitalSignature(dr.sign, pbkey_response_dec.pub_key);
        boolean verfied=verify_sign(sign_from_cl1);
        if(verfied)
        System.out.println("\nSignature verfied");
        else
        System.out.println("\nSignature not verfied, document has changed");
    }
    void timestamp(AuthenticationServer as,TimestampingServer ts)
    {   System.out.println("\nTimestamping");
        start();

        AuthenticationRequest ar=send_request_to_as(1);
        System.out.println("\nRequest sent to Authentication Server : < "+ar.id+" || "+ar.service_type+" >");

        ArrayList<String> res_from_as_enc=as.service_request(ar);
        AuthenticationResponse auth_res = AuthenticationResponse.decAuthenticationResponse(res_from_as_enc,as_symm_key);
        System.out.println("\nDecrypted response from Authentication Server : \n< "+auth_res.symm_key+"  ||  "+auth_res.server_id+"  ||  "+auth_res.enc_auth_ticket.get(0)+" || "+auth_res.enc_auth_ticket.get(1)+" || "+auth_res.enc_auth_ticket.get(2)+" >");
        ClientTimestampingRequest cl_ts_req=new ClientTimestampingRequest(client_id,doc_hash);
        System.out.println("\nRequest framed at client :  < "+cl_ts_req.id+" || "+cl_ts_req.doc_hash+" >");
        ArrayList<String> cl_ts_req_enc=cl_ts_req.encClientTimestampingRequest(auth_res.symm_key);
        TimestampingRequest t_req=new TimestampingRequest(client_id,auth_res.enc_auth_ticket,cl_ts_req_enc);
        System.out.println("\nTimestamping request sent to Timestamping Server :\n< "+t_req.client_id+" || "+t_req.auth_ticket.get(0)+" || "+t_req.auth_ticket.get(1)+" || "+t_req.auth_ticket.get(2)+" || "+t_req.client_request.get(0)+" || "+t_req.client_request.get(1)+" >");
        ArrayList<String> ts_res_enc=ts.timestamping_request(t_req);

        TimestampingResponse ts_res_decrypted=TimestampingResponse.decTimestampingResponse(ts_res_enc, auth_res.symm_key);
        System.out.println("Timestamping response decrypted at client:\n< "+ts_res_decrypted.doc_hash+" || "+ts_res_decrypted.gmt_timestamp+" || "+ts_res_decrypted.server_id+" || "+ts_res_decrypted.sign.get(0)+" || "+ts_res_decrypted.sign.get(1)+" || "+ts_res_decrypted.sign.get(2)+" || "+ts_res_decrypted.sign.get(3)+" >");

        store_Doc_response(ts_res_decrypted);

    }
    void start()
    {   doc_path="./Assignment4 NS.pdf";
        genHash();
        
    }
    AuthenticationRequest send_request_to_as(int serviceType)
    {
        AuthenticationRequest ar=new AuthenticationRequest(client_id,serviceType);
        return ar;
    }
    
    void store_Doc_response(TimestampingResponse ts_res)
    {  
        dr=new DocResponse(ts_res.doc_hash,ts_res.gmt_timestamp,ts_res.server_id,ts_res.sign);
    }
    void request_ts(ArrayList<String> ticket_from_as){

    }
    void request_doc(Client cl1)
    {   
        cl1.send_doc(this);
    }
    void send_doc(Client obj){
        //return timestamped_doc;
        obj.dr=dr;
    }
    boolean verify_sign(DigitalSignature signature)
    {   if(signature.doc_hash.equals(dr.doc_hash)&&signature.gmt_timestamp.equals(dr.gmt_timestamp))
        return true;
        return false;
    }
    void request_ca(String ticket_from_as){

    }
    /*public static void main(String[] args){
    Client cl1=new Client();
    cl1.start();
    System.out.println(cl1.doc_hash);
    
    }*/
}
