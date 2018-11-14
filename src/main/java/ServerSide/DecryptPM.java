package ServerSide;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

public class DecryptPM {

    private static final String AES = "AES";

    /**
     * encrypt a value and generate a keyfile
     * if the keyfile is not found then a new one is created
     * @throws GeneralSecurityException
     * @throws IOException
     */

    static String path = System.getProperty("user.dir");
    static String resourcePath = path+"\\src\\main\\resources\\";

    public static String encrypt(String value, File keyFile)
            throws GeneralSecurityException, IOException{
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(DecryptPM.AES);
            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
            fw.close();
        }

        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(DecryptPM.AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * decrypt a value
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private static String decrypt(String message, File keyFile)
            throws GeneralSecurityException, IOException{
        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(DecryptPM.AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
        return new String(decrypted);
    }

    private static SecretKeySpec getSecretKeySpec(File keyFile)
            throws NoSuchAlgorithmException, IOException{
        byte [] key = readKeyFile(keyFile);
        return new SecretKeySpec(key, DecryptPM.AES);
    }

    private static byte [] readKeyFile(File keyFile)
            throws FileNotFoundException {
        Scanner scanner =
                new Scanner(keyFile).useDelimiter("\\Z");
        String keyValue = scanner.next();
        scanner.close();
        return hexStringToByteArray(keyValue);
    }


    private static String byteArrayToHexString(byte[] b){
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++){
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++){
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte)v;
        }
        return b;
    }

    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
        finally {
            br.close();
        }
    }

    private static void createKeyFile(String KeyValue){
        try {
            String KEY_FILE = resourcePath+"DecryptKeyFile.key";
            File F1 = new File(KEY_FILE);
            if (F1.createNewFile()) {
                FileWriter fws = new FileWriter(KEY_FILE);
                fws.write(KeyValue);
                fws.flush();
                fws.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
    NOTE:
    For Decrypt to work the expected requirement is availability of Key and Enc data file in resource folder for server side
     ***/

    public static String decryptData() throws IOException, GeneralSecurityException {



            final String MID_FILE = resourcePath+"PmEnc.properties";

        Properties p2 = new Properties();
        /* To enter key manually
        String InputKey = JOptionPane.showInputDialog("Type your message here");
         */
        p2.load(new FileReader(MID_FILE));
        String keyData = p2.getProperty("KV");
        createKeyFile(keyData);
        final String KEY_FILE = resourcePath+"DecryptKeyFile.key";
        String encryptMessage = p2.getProperty("MID");
        System.out.println("Encrypted DATA OF PD1 via P2: \n"+encryptMessage);
        String DecryptMID = DecryptPM.decrypt(encryptMessage, new File(KEY_FILE));
        return DecryptMID;
    }
}
