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
            System.out.println("hash in client"+doc_hash);
            //return hashtext;
        }
        catch(Exception e)
        {

        }
    }
    void start()
    {   doc_path="./Assignment4 NS.pdf";
        genHash();
        //send_request_to_as(1);
    }
    AuthenticationRequest send_request_to_as(int serviceType){
        AuthenticationRequest ar=new AuthenticationRequest(client_id,serviceType);
        return ar;
        }
    
    void store_Doc_response(TimestampingResponse ts_res)
    {   /*ArrayList<String> signature=new ArrayList<String>();
        signature.add(ts_res.get(3));
        signature.add(ts_res.get(4));
        signature.add(ts_res.get(5));
        signature.add(ts_res.get(6));
        DocResponse doc_res=new DocResponse(ts_res.get(0),ts_res.get(1),ts_res.get(2),signature);
        dr=doc_res;*/
        dr=new DocResponse(ts_res.doc_hash,ts_res.gmt_timestamp,ts_res.server_id,ts_res.sign);
    }
    void request_ts(ArrayList<String> ticket_from_as){

    }
    void request_doc(){

    }
    void send_doc(Client obj){
        //return timestamped_doc;
        obj.dr=dr;
    }
    boolean verify_sign(String signature){
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
