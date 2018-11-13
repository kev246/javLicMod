package App.LicenseClient;

import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static App.LicenseClient.SymEncPM.encrypt;

public class ActKey extends Base {


    /*
    On receiving Activation Key (decrypted msg only) inputting on App, i should create a new encrypted data (PN)
    using Pm's Key file and also should be signed property file unlike Pd or Pm. */
    public ActKey(){
        if (CheckFlagStat.checkFileForActKeyStat()){
            startAct();
        }else{
            infoBox("Input Key received for activation from tool settings to continue further with Activation","INFO:");
        }
    }
    private static void makePnEncSigned(String ActivationKey) throws GeneralSecurityException, IOException {
        final String PN_FILE = "KAEncrypted.properties";
        final String PM_KEYFILE = "abc/NewKeyFile.key";

        //Generate new encrypt file with new data and old key
        String newEncryptData = encrypt(ActivationKey, new File(PM_KEYFILE));
        Properties PnEnc = new Properties();
        PnEnc.put("LicenseData:", newEncryptData);
        PnEnc.store(new FileWriter(PN_FILE),"Pn file created withPmKey");

    }

    public void startAct() {
        String InputKey = JOptionPane.showInputDialog("Type your message here");
        try {
            makePnEncSigned(InputKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
