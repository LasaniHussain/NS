import java.io.*;
import java.math.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
//import org.apache.pdfbox.pdmodel.*;
//import org.apache.pdfbox.util.*;
public class readpdf 
{   public static SecretKey genDesKey()
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
  
  
  
  public static String encrypt(String str,SecretKey key) {
 
        try 
        {
          Cipher ecipher = Cipher.getInstance("DES");
 
            // initialize the ciphers with the given key
 
          ecipher.init(Cipher.ENCRYPT_MODE, key);
 
          //dcipher.init(Cipher.DECRYPT_MODE, key);
 
          //String encrypted = encrypt("This is a classified message!");
 
          //String decrypted = decrypt(encrypted);
 
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
 
            // initialize the ciphers with the given key
 
            dcipher.init(Cipher.DECRYPT_MODE, key);
 
          //String encrypted = encrypt("This is a classified message!");
 
          //String decrypted = decrypt(encrypted);
 
        //System.out.println("Decrypted: " + decrypted);
        // decode with base64 to get bytes
          //byte[] dec = BASE64DecoderStream.decode(str.getBytes());
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
    public static String getHash()
    {
      /*File file = new File("/home/lasani/IIIT Delhi/Network Security/Assignment 4/Assignment4 NS.pdf");
      PDDocument document = PDDocument.load(file);
      //Instantiate PDFTextStripper class
      PDFTextStripper pdfStripper = new PDFTextStripper();
      //Retrieving text from PDF document
      String text = pdfStripper.getText(document);
      //System.out.println(text);
      MessageDigest md5;
      try 
      {
        md5 = MessageDigest.getInstance("MD5");
      } 
      catch (NoSuchAlgorithmException e) 
      {
        throw new IllegalStateException(e.getMessage(), e);
      }
      finally
      {
        document.close();
      }
    md5.reset();
    md5.update(text.getBytes());
    byte[] digest = md5.digest();
    BigInteger bigInt = new BigInteger(1,digest);
    String hashtext = bigInt.toString(16);
    return hashtext;
      //Closing the document
    }*/
   public static void main(String args[]) throws IOException 
   {
      //Loading an existing document
      SecretKey sym_key=genDesKey();
      String encrypted=encrypt("Hello there !", sym_key);
      System.out.println(encrypted);
      String decrypted=decrypt(encrypted, sym_key);
      System.out.println(decrypted);
   }
}