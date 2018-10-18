package App.LicenseClient;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static Liscense.encDecrExample.encrypt;

public class keyActivation {

    /*
    on receiving Activation Key (decrypted msg only) inputting on App, i should create a new encrypted data (PN)
    using Pm's Key file and also should be signed property file unlike Pd or Pm. */
    public void makePnEncSigned(String ActivationKey) throws GeneralSecurityException, IOException {
        String PN_FILE = "PNEncrypted.properties";
        String PM_KEYFILE = "EncKeyFile.key";

        //Generate new encrypt file with new data and old key
        String newEncryptData = encrypt(ActivationKey, new File(PM_KEYFILE));
        Properties PnEnc = new Properties();
        PnEnc.put("LicenseData:", newEncryptData);
        PnEnc.store(new FileWriter(PN_FILE),"Pn file created withPmKey");

    }
    //TODO method for need to sign this file as well tobe written (before or after encryption?)

    public void signFile(){}
}
