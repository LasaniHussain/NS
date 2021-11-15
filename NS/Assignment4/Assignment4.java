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
        ArrayList<String> res_from_as_dec=des.dec_list(as.service_request(ar),cl1.as_symm_key);
        System.out.println(res_from_as_dec.size());
        for(int i=0;i<res_from_as_dec.size();i++)
        System.out.println(res_from_as_dec.get(i));
    }
}