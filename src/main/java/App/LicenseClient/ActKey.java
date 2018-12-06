package App.LicenseClient;

import javax.swing.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static App.LicenseClient.SymEncPM.encrypt;

/***
 * ActKey Class helps in starting an activation flow which includes:
 * 1. Taking user input as Activation Key
 * 2. Creating Encrypt file using old key data and activation key as message.
 */
class ActKey extends Base {

    ActKey(){
        if (CheckFlagStat.checkFileForActKeyStat()){
            startAct();
        }else{
            infoBox("Looking for available key information...","INFO:");
        }
    }

    /***
     * makePnEncSigned method helps in creating the required KAEncrypted file and loading it with data of encrypted
     * activation content in LicenseData key value. also rewriting the important demo key with key obtained from digital sign file
     * @param ActivationKey given to clients on purchase
     * @throws GeneralSecurityException if Key file for creating new Encrypt data is missing
     * @throws IOException if required files are missing
     */
    private static void makePnEncSigned(String ActivationKey) throws GeneralSecurityException, IOException {
        final String PN_FILE = "KAEncrypted.properties";
        final String PM_KEYFILE = "abc/NewKeyFile.key";

        //Generate new encrypt file with new data and old key
        String newEncryptData = encrypt(ActivationKey, new File(PM_KEYFILE));
        Properties PnEnc = new Properties();
        PnEnc.put("LicenseData:", newEncryptData);

        Verifications vd = new Verifications();

        try {
            PnEnc.setProperty("KV",vd.dContent("MyData/SignedData.txt", "MyKeys/publicKey"));
        } catch (Exception e) {
            infoBox("unable to locate datafile for activating full key","ERROR:");
            e.printStackTrace();
        }

        PnEnc.store(new FileWriter(PN_FILE),"Pn file created withPmKey");

    }

    /***
     * startAct method helps in starting the whole activation flow from taking inputs to creating required files for
     * activation and rewriting the demo key with valid key for verification later on.
     */
    protected static void startAct() {
        String InputKey = JOptionPane.showInputDialog("Please enter your Activation Key below:");
        if (InputKey != null && !InputKey.isEmpty()) {
            try {
                makePnEncSigned(InputKey);
                makeflaggerActValueTrue();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"Activation window closed/cancelled ! \n           " +
                    "              OR \n         " +
                    "     No key inputted\n          " +
                    "     Please try later.");
            makeFlaggerActValueFalse();
        }
    }
}
