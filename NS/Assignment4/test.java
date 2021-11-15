import java.util.ArrayList;

import javax.crypto.SecretKey;

public class test {
    public static void main(String[] args) {
        DES des = new DES();
        SecretKey s = des.genDesKey();
        SecretKey n = des.genDesKey();
        AuthenticationTicket at = new AuthenticationTicket(s, "100", "200");
        ArrayList<String> st = at.encryptAuthenticationTicket(n);
        AuthenticationTicket ne = AuthenticationTicket.decAuthenticationTicket(st, n);
        assert at.equals(ne): "equal";
        System.out.println(at.symm_key+" "+ne.symm_key);

    }
}
