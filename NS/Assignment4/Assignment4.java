import java.util.*;
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
    String service_request(String id, int serviceType){
        return "";
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
    String rsa_public_key;//maybe not needed here
    private String rsa_private_key;
    void addPrivateKey(String private_key){}
    TimestampingServer()
    {
        Random rn = new Random();
        id= String.valueOf(100+rn.nextInt(100));
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
public class Assignment4{
    public static SecretKey genDesKey()
    {   SecretKey key=null;
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
    public static String encrypt(String str,SecretKey key) 
    {
 
        try 
        {
            Cipher ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            //System.out.println("Decrypted: " + decrypted);
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            // encode to base64
            //enc = BASE64EncoderStream.encode(enc);
            enc=Base64.getEncoder().encode(enc);
            return new String(enc);
        }
        catch (Exception e) 
        {
        e.printStackTrace();
        }
        return null;
    }
 
    public static String decrypt(String str,SecretKey key) 
    {
        try 
        {
            Cipher dcipher = Cipher.getInstance("DES");

            dcipher.init(Cipher.DECRYPT_MODE, key);

            byte[] dec=Base64.getDecoder().decode(str.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);
          // create new string based on the specified charset
            return new String(utf8, "UTF8");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    static void init(Client cl1,Client cl2,TimestampingServer ts,PublicKeyServer pks,AuthenticationServer as){
        //symm key between client1 and AS
        SecretKey cl1_AS=genDesKey();
        as.addSymmKey(cl1.client_id, cl1_AS);;
        
        //symm key between client2 and AS
        SecretKey cl2_AS=genDesKey();
        as.addSymmKey(cl2.client_id, cl2_AS);;
        
        //symm key between PKS and AS
        SecretKey PKS_AS=genDesKey();
        as.addSymmKey(pks.id, PKS_AS);
        
        //symm key between TS and AS
        SecretKey TS_AS=genDesKey();
        as.addSymmKey(ts.id, TS_AS);

        as.DiplaySymmKeys();
        //
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