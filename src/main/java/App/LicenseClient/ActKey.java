package App.LicenseClient;

import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static App.LicenseClient.SymEncPM.encrypt;

class ActKey extends Base {


/***
    On receiving Activation Key (decrypted msg only) inputting on App, i should create a new encrypted data (PN)
    using Pm's Key file and also should be signed property file unlike Pd or Pm. */

ActKey(){
        if (CheckFlagStat.checkFileForActKeyStat()){
            startAct();
        }else{
            infoBox("Looking for available key information...","INFO:");
        }
    }
    private static void makePnEncSigned(String ActivationKey) throws GeneralSecurityException, IOException {
        final String PN_FILE = "KAEncrypted.properties";
        final String PM_KEYFILE = "abc/NewKeyFile.key";

        //Generate new encrypt file with new data and old key
        String newEncryptData = encrypt(ActivationKey, new File(PM_KEYFILE));
        Properties PnEnc = new Properties();
        PnEnc.put("LicenseData:", newEncryptData);

/***Actually activating internally via the digital certificate content itself but given an INPUT BOX to
        enter for users also which is for the decrypted data that we give back to enter so that user creates the same PN as PM using old key*/


        VerifyDigitalSign vd = new VerifyDigitalSign();
        try {
            PnEnc.setProperty("KV",vd.dContent("MyData/SignedData.txt", "MyKeys/publicKey"));
        } catch (Exception e) {
            System.out.println("file not found");
            e.printStackTrace();
        }
        PnEnc.store(new FileWriter(PN_FILE),"Pn file created withPmKey");

    }

    protected static void startAct() {
        String InputKey = JOptionPane.showInputDialog("Please enter your Activation Key below:");
        if (InputKey != null) {
            try {
                makePnEncSigned(InputKey);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"Activation window closed ! \n please try again");
        }
    }
}
