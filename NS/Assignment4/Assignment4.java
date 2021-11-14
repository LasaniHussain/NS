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

class Client{
    String as_symm_key;
    String client_id;
    String timestamped_doc;//this can either by the doc timestamped by ts or recieved form another client
    
    Client()
    {   Random rn = new Random();
        client_id= String.valueOf(rn.nextInt(100));
    }
    void start(){

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
class RSA{

    KeyPair genRSAKeys(){
        KeyPair kpg = null;
        try {
            KeyPairGenerator k = KeyPairGenerator.getInstance("RSA");
            k.initialize(2048);
            kpg = k.genKeyPair();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return kpg;
    }

    String encryption(String plaintext,Key key){
        
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] secretMessageBytes = plaintext.getBytes("UTF8");
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    String decryption(String ciphertext, Key key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] secretMessageBytes = ciphertext.getBytes("UTF8");
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
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

    String encrypt(String str, Cipher cipher) {
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
       
    String decrypt(String str, Cipher cipher) {
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
public class Assignment4{
    
    static void init(Client cl1,Client cl2,TimestampingServer ts,PublicKeyServer pks,AuthenticationServer as){
        //symm key between client1 and AS
        DES des = new DES();
        SecretKey cl1_AS=des.genDesKey();
        as.addSymmKey(cl1.client_id, cl1_AS);;
        
        //symm key between client2 and AS
        SecretKey cl2_AS=des.genDesKey();
        as.addSymmKey(cl2.client_id, cl2_AS);;
        
        //symm key between PKS and AS
        SecretKey PKS_AS=des.genDesKey();
        as.addSymmKey(pks.id, PKS_AS);
        
        //symm key between TS and AS
        SecretKey TS_AS=des.genDesKey();
        as.addSymmKey(ts.id, TS_AS);

        as.DiplaySymmKeys();
        as.pk_id = pks.id;
        as.ts_id = ts.id;
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
        //System.out.println();
        /*File file = new File("/home/lasani/IIIT Delhi/Network Security/Assignment 4/Assignment4 NS.pdf");
      PDDocument document = PDDocument.load(file);
      //Instantiate PDFTextStripper class
      PDFTextStripper pdfStripper = new PDFTextStripper();
      //Retrieving text from PDF document
      String text = pdfStripper.getText(document);
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
    String hashtext = bigInt.toString(16);
    System.out.println(hashtext);
      //Closing the document
      document.close();*/
    }
}