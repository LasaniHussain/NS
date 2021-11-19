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
        //as.DiplaySymmKeys();
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
        //System.out.println("public key gen : "+ts.rsa_public_key);
        //pks.addPublicKey(ts.id, ts.rsa_public_key);
        AuthenticationServer as=new AuthenticationServer();
        System.out.println("Client 1 id : "+cl1.client_id);
        System.out.println("Client 2 id : "+cl2.client_id);
        System.out.println("Timestamping server id : "+ts.id);
        System.out.println("Public Key server id : "+pks.id);
        System.out.println("Authentication server id : "+as.id);
        //System.out.println(cl1.client_id+" "+cl2.client_id+" "+ts.id+" "+pks.id+" "+as.id);
        init(cl1,cl2,ts,pks,as);
        //flow 1

        cl1.timestamp(as,ts);
        

        // part 2
        cl2.verify_timestamp(cl1,pks,ts,as);
        

    }
}