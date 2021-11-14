import java.security.*;
public class test {
    public static void main(String[] args) {
        RSA rsa = new RSA();
        KeyPair kpg = rsa.genRSAKeys();
        Key rsa_public_key = kpg.getPublic();
        Key rsa_private_key = kpg.getPrivate();
        String s = "hi";
        String cipher = rsa.encryption(s, rsa_private_key);
        System.out.println("cipher:"+cipher);
        System.out.println(cipher.length());
        String d = rsa.decryption(cipher, rsa_public_key);
        System.out.println("plaintext:"+d);
    }
}
