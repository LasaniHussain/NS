import java.util.*;
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
