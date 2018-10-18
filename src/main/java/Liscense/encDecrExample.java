package Liscense;


import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class encDecrExample {

    public static final String AES = "AES";

    /**
     * encrypt a value and generate a keyfile
     * if the keyfile is not found then a new one is created
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String encrypt(String value, File keyFile)
            throws GeneralSecurityException, IOException{
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(encDecrExample.AES);
            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
            fw.close();
        }

        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(encDecrExample.AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }
//TODO need to modify this method to make proper checks for digital sign validation and enc validation using pnKey made from Pn create class
    public static boolean verifyLis() throws GeneralSecurityException, IOException {
        boolean returnValue = false;
        final String PM_FILE = "PMEncrypted.properties";
        final String PN_FILE = "PNEncrypted.properties";
        Properties Pmf = new Properties();
        Pmf.load(new FileReader(PM_FILE));

        Properties Pnf = new Properties();
        Pnf.load(new FileReader(PN_FILE));
        String newEncryptData = Pnf.getProperty("LicenseData:");
        String oldEncryptData = Pmf.getProperty("LicenseData:");
            //Check if both encrypt strings are same return true
        returnValue = newEncryptData.equalsIgnoreCase(oldEncryptData);
        return returnValue;    }

    /**
     * decrypt a value
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt(String message, File keyFile)
            throws GeneralSecurityException, IOException{
        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(encDecrExample.AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
        return new String(decrypted);
    }

    private static SecretKeySpec getSecretKeySpec(File keyFile)
            throws NoSuchAlgorithmException, IOException{
        byte [] key = readKeyFile(keyFile);
        SecretKeySpec sks = new SecretKeySpec(key, encDecrExample.AES);
        return sks;
    }

    private static byte [] readKeyFile(File keyFile)
            throws FileNotFoundException{
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
    public static void main(String[] args) throws Exception {
        final String KEY_FILE = "EncKeyFile.key";
        final String NEW_KEY = "NewKeyFile.key";
        final String PWD_FILE = "encrypted.properties";
        final String NEW_ENCFILE = "newEncSameData.properties";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\main\\resources\\licenseInfo.properties";
        System.out.println(path);


        String mainFile = readFile(path);
        Properties p1 = new Properties();

        String encryptedFile = encDecrExample.encrypt(mainFile, new File(KEY_FILE));
        p1.put("LicenseData:", encryptedFile);
        p1.store(new FileWriter(PWD_FILE), "Encrypted data's Properties file is now created...!");
        System.out.println("Encrypted DATA OF PD1 via P1: \n"+p1.getProperty("LicenseData:"));
        Properties p2 = new Properties();
        String NewEncOldDataNewKey = encDecrExample.encrypt(mainFile, new File(KEY_FILE));
        p2.put("LicenseData:", NewEncOldDataNewKey);
        p2.store(new FileWriter(NEW_ENCFILE),"Encrypted data with new key..");
        System.out.println("Encrypted DATA OF PD1 via P1: \n"+p2.getProperty("LicenseData:"));

        // ==================
       /* Properties p2 = new Properties();

        p2.load(new FileReader(PWD_FILE));
        encryptedFile = p2.getProperty("LicenseData:");
        System.out.println("Encrypted DATA OF PD1 via P2: \n"+encryptedFile);*/
        System.out.println("Decrypted value of PD1 is : \n"+encDecrExample.decrypt(encryptedFile, new File(KEY_FILE)));
    }
}