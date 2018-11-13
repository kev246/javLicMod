package App.LicenseClient;

import DigitalSigner.GenerateKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

class RequestLic extends Base {

    /*
    TODO Write logic to create PM (Encrypted) from PD (default prop) using machine motherboard ID

        m1: obtain motherboardID
        m2: Creating PM ===>>
        multiply mid with current PID encrypt data and now encrypt that string with 2 property LiscenceID: and MID into a property file(PM) created.
        Store it in special location as this will be later used to compared lis for comparing msg (no need to decrypt)
    TODO Send Pm to server side PM which contains Unique new Lis, MID, encrypted within PM.
    TODO Make key also unique by modifying it (add mid into key value at the end ,to make a new string not enc but string and then use that as key file later on
     */
    RequestLic() {
        if (checkReqFileStatus.flag) {
            try {
                ComputerIdentifier Ci = new ComputerIdentifier();
                GenerateKeys gk = new GenerateKeys(1024);
                gk.createKeys();
                gk.writeToFile("MyKeys/publicKey", gk.getPublicKey().getEncoded());
                gk.writeToFile("src/main/resources/privateKey", gk.getPrivateKey().getEncoded());
                Ci.createMidFile();
                checkReqFileStatus.flag = false;
                infoBox("Please Send over the 2 files at resources location to get Activation File","Action required !");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("No request for activation looking at available key info...");
    }

}
