package App.LicenseClient;


import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/***
 * SymEnc Class is the first level of symmetric encryption technique implemented within the module for encrypting the
 * machineID data generated
 */

public class SymEncPM {

    private static final String AES = "AES";

    /***
     * encrypt method helps in symmetric encryption for the machine ID created using AES algorithm
     * @param value
     * @param keyFile
     * @return String of encrypted data
     * @throws GeneralSecurityException
     * @throws IOException
     */
    static String encrypt(String value, File keyFile)
            throws GeneralSecurityException, IOException{
        if (!keyFile.exists()) {

            KeyGenerator keyGen = KeyGenerator.getInstance(SymEncPM.AES);
            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
            fw.close();
        }

        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(SymEncPM.AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /***
     * The SecretKeySpec method helps in reading the key File data on to a byte Array and later returns an instance
     * of SecretKeySpec which is only instantiated using byte Array data and Algorithm used.
     * @param keyFile key file generated from symmetric encryption instantiation.
     * @return  SecretKeySpec Object
     * @throws NoSuchAlgorithmException if the mentioned algorithm is not found
     * @throws IOException if file is missing
     */
    private static SecretKeySpec getSecretKeySpec(File keyFile)
            throws NoSuchAlgorithmException, IOException{
        byte [] key = readKeyFile(keyFile);
        SecretKeySpec sks = new SecretKeySpec(key, SymEncPM.AES);
        return sks;
    }

    /***
     * readKeyFile method helps in reading the Hex string data stored in key file into a byte array for easy usage in other methods
     * @param keyFile key file as input from Symmetric encryption
     * @return byte Array data
     * @throws FileNotFoundException if file is missing or not found
     */
    private static byte [] readKeyFile(File keyFile)
            throws FileNotFoundException{
        Scanner scanner =
                new Scanner(keyFile).useDelimiter("\\Z");
        String keyValue = scanner.next();
        scanner.close();
        return hexStringToByteArray(keyValue);
    }

    /***
     * byteArrayToHexString method simply helps in convering a given byte array into an Hex String data
     * @param b input of byte array
     * @return Hex string data
     */
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

    /***
     * hexStringToByteArray method simply helps in converting a given Hex string to Byte Array data
     * @param s input of string as parameter
     * @return byte array data
     */
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
   /* public static void main(String[] args) throws Exception {
        final String KEY_FILE = "EncKeyFile.key";
        final String NEW_KEY = "NewKeyFile.key";
        final String PWD_FILE = "encrypted.properties";
        final String NEW_ENCFILE = "newEncSameData.properties";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\main\\resources\\flagger.properties";
        System.out.println(path);


        String mainFile = readFile(path);
        Properties p1 = new Properties();

        String encryptedFile = SymEncPM.encrypt(mainFile, new File(KEY_FILE));
        p1.put("LicenseData:", encryptedFile);
        p1.store(new FileWriter(PWD_FILE), "Encrypted data's Properties file is now created...!");
        System.out.println("Encrypted DATA OF PD1 via P1: \n"+p1.getProperty("LicenseData:"));
        Properties p2 = new Properties();
        String NewEncOldDataNewKey = SymEncPM.encrypt(mainFile, new File(KEY_FILE));
        p2.put("LicenseData:", NewEncOldDataNewKey);
        p2.store(new FileWriter(NEW_ENCFILE),"Encrypted data with new key..");
        System.out.println("Encrypted DATA OF PD1 via P1: \n"+p2.getProperty("LicenseData:"));

        // ==================
        Properties p2 = new Properties();

        p2.load(new FileReader(PWD_FILE));
        encryptedFile = p2.getProperty("LicenseData:");
        System.out.println("Encrypted DATA OF PD1 via P2: \n"+encryptedFile);
        System.out.println("Decrypted value of PD1 is : \n"+ SymEncPM.decrypt(encryptedFile, new File(KEY_FILE)));
    }*/
}