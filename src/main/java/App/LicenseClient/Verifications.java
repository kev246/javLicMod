package App.LicenseClient;


import java.io.*;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Properties;

/***
 * Verifications class helps in verifying all checks before every time the application is started to ensure and start the
 * correct version of the application (demo or full mode)
 */
class Verifications extends Base {
        private List<byte[]> list;

    /***The Verify method retrieves the byte arrays from the File and returns TRUE only if the signature is verified.
     */
        @SuppressWarnings("unchecked")
        //The constructor of VerifyMessage class retrieves the byte arrays from the File and prints the message only if the signature is verified.
        public boolean verify(String filename, String keyFile) throws Exception {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.list = (List<byte[]>) in.readObject();
            in.close();

            //System.out.println(verifySignature(list.get(0), list.get(1), keyFile) ? "VERIFIED MESSAGE" + "\n----------------\n" + new String(list.get(0)) : "Could not verify the signature.");
            return verifySignature(list.get(0), list.get(1), keyFile);

        }

        /***
        if dContent has the hard coded value as output then digital sign is wrong and not validated but still we can understand this is the case as hardcode value is
         false only if the verification fails
         */
        @SuppressWarnings("unchecked")
        String dContent(String filename, String keyFile) throws Exception {
            String val = "";
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.list = (List<byte[]>) in.readObject();
            in.close();

            if (verifySignature(list.get(0), list.get(1), keyFile)){
                val = new String(list.get(0));
            }else {
                val = "XXXXXXXXXXZZZZZZZZZZZZZZYYYYYYYYDDDDDDDDDDSSSSSSSSSSSS";
            }
            return val;
        }

    /***
     * verifySignature method helps check the digital signature verification on each run of the application.
     * Need files of specific type as inputs in specific folders
     * @param data value from byte array
     * @param signature data from byte array
     * @param keyFile key file from asymmetric for digital sign verification
     * @return boolean value stating the digital signature verification is true or false
     * @throws Exception if any of the required file is missing which contains digital sign verification information.
     */
        //Method for signature verification that initializes with the Public Key, updates the data to be verified and then verifies them using the signature
        private boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(getPublic(keyFile));
            sig.update(data);

            return sig.verify(signature);
        }

    /***
     * verifyK method helps to verify if the 3 types of key value generated before and after activation request is the
     * same data or not.
     * @return boolean value to check / verify if keys are same for a given machine
     * @throws Exception generally if mentioned files are missing in folder path
     */
    boolean verifyK() throws Exception {
            String x = loadPropertyFile("KAEncrypted.properties").getProperty("KV");
            String y = ComputerIdentifier.halfLengthData();
            String z = dContent("MyData/SignedData.txt", "MyKeys/publicKey");
            return x.equals(y) && y.equals(z) && z.equals(x);
        }

    /***
     * verifyPnPmData method is one of the 3 levels of verification done , it basically checks the value created using
     * encryption for machine ID using that specific key is same as the data created with same key after Activation key is
     * passed on to user.
     * @return boolean value showing if the verification is Pass or Fail
     * @throws GeneralSecurityException if required files are missing     */
    boolean verifyPnPmData() throws GeneralSecurityException {

        final String PM_FILE = "KAEncrypted.properties";
        final String PN_FILE = "src/main/resources/PmEnc.properties";
        boolean returnValue;
        Properties Pmf = new Properties();
        try {
            Pmf.load(new FileReader(PM_FILE));
        } catch (IOException e) {
            infoBox("To use App in Full mode need activation","Activation pending!");
        }

        Properties Pnf = new Properties();
        try {
            Pnf.load(new FileReader(PN_FILE));
        } catch (IOException e) {
            infoBox("To use App in Full mode need activation","Activation pending!");
        }
        String newEncryptData = Pnf.getProperty("MID");
        if (newEncryptData== null){
            newEncryptData =  "defaultValue";
        }
        String oldEncryptData = Pmf.getProperty("LicenseData:");
        //Check if both encrypt strings are same return true
        returnValue = newEncryptData.equalsIgnoreCase(oldEncryptData);
        return returnValue;
    }

        //Method to retrieve the Public Key from a file
        private PublicKey getPublic(String filename) throws Exception {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }



    }
