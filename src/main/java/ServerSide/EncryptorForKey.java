package ServerSide;
import App.LicenseClient.SymEncPM;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class EncryptorForKey {


        public static String encrypt(String key, String randomVector, String value) {
            try {
                IvParameterSpec iv = new IvParameterSpec(randomVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                byte[] encrypted = cipher.doFinal(value.getBytes());
                System.out.println("encrypted text: "  + Base64.encodeBase64String(encrypted));
                return Base64.encodeBase64String(encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String decrypt(String key, String randomVector, String encrypted) {
            try {
                IvParameterSpec iv = new IvParameterSpec(randomVector.getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] originalText = cipher.doFinal(Base64.decodeBase64(encrypted));
                System.out.println("decrypted text: "  + new String(originalText));
                return new String(originalText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void main(String[] args) {
            String key = "JavasEncryptDemo"; // 128 bit key
            String randomVector = "RandomJavaVector"; // 16 bytes IV
            decrypt(key, randomVector, encrypt(key, randomVector, "Karthik"));

        }
    }
