import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class test {
    public static void main(String[] args) {
        RSA r = new RSA();
        KeyPair k = r.genRSAKeys();
        PublicKey pk = k.getPublic();
        PrivateKey pr = k.getPrivate();
        DES des = new DES();
        SecretKey sk = des.genDesKey();
        //String s = "hellow how are you?";
        System.out.println(KeyConversion.convertDESToString(sk).length());
        String enc = r.encryption(KeyConversion.convertDESToString(sk), pr);
        System.out.println(enc.length());
        SecretKey dec = KeyConversion.convertToDESKey(r.decryption(enc, pk));
        assert sk.equals(dec): "equal";

    }
}
