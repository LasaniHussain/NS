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
        String s = "hellow how are you?";
        String enc = r.encryption(s, pr);
        String dec = r.decryption(enc, pk);
        assert s.equals(dec): "equal";

    }
}
