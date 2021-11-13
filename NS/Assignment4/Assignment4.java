import java.util.*;
class AuthenticationServer{
    private Hashtable<String,String> symm_keys = new Hashtable<String,String>();
    String id;
    void addSymmKey(String id,String symm_key){

    }
    String service_request(String id, int serviceType){
        return "";hello
    }
}
class Client{
    String as_symm_key;
    String client_id;
    String timestamped_doc;//this can either by the doc timestamped by ts or recieved form another client
    void start(){

    }
    void send_request_to_as(int serviceType){
        add
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
    void addPrivateKey(String private_key){

    }
    String timestamping_request(String id, String ticket,String request){
        return "";
    }
}
class PublicKeyServer{//changing name for CA
    String id;
    String as_symm_key;
    private Hashtable<String,String> public_keys = new Hashtable<String,String>();
    void addPublicKey(String id,String public_key){

    }
    String public_key_request(String id, String ticket,String request){
        return "";
    }
}
public class Assignment4{
    static void init(){

    }
    public static void main(String[] args){
        init();
        File file = new File("/home/lasani/IIIT Delhi/Network Security/Assignment 4/Assignment4 NS.pdf");
      PDDocument document = PDDocument.load(file);
      //Instantiate PDFTextStripper class
      PDFTextStripper pdfStripper = new PDFTextStripper();
      //Retrieving text from PDF document
      //String text = pdfStripper.getText(document);
    }
}