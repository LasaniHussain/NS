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
    String timestamped_doc;//this can either by the doc timestamped by ts or recieved form another client
    String doc_hash;
    Client()
    {   Random rn = new Random();
        client_id= String.valueOf(rn.nextInt(100));
    }
    void genHash(String path)
    {
        
        try
        {
            File file = new File(path);
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
            //return hashtext;
        }
        catch(Exception e)
        {

        }
    }
    void start()
    {
        genHash("/home/lasani/IIIT Delhi/Network Security/Assignment 4/NS/NS/Assignment4/Assignment4 NS.pdf");
    }
    void send_request_to_as(int serviceType){
            
        }
    void request_ts(String ticket_from_as){

    }
    void request_doc(){

    }
    String send_doc(){
        return timestamped_doc;
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
