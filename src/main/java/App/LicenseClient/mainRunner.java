package App.LicenseClient;

import DigitalSigner.VerifyDigitalSign;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class MainRunner extends Base {

    private final static String DKey = "WEFCDPEFWASOFNWEFOI32984SDFJOWER";
    private static boolean returnValue = false;
    //TODO need to modify this method to make proper checks for digital sign validation and enc validation using pnKey made from Pn create class
    private static boolean verifyLis() throws GeneralSecurityException {
        final String PM_FILE = "KAEncrypted.properties";
        final String PN_FILE = "src/main/resources/PmEnc.properties";
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
        String oldEncryptData = Pmf.getProperty("LicenseData:");
        //Check if both encrypt strings are same return true
        returnValue = newEncryptData.equalsIgnoreCase(oldEncryptData);
        return returnValue;
    }

    public static void main(String[] args) throws Exception {
        //Checking if valid request is present
        new RequestLic();
        //Else Case
        Properties Pn;
        Pn = loadPropertyfile("KAEncrypted.properties");
        String key = Pn.getProperty("KV");
        //Checking key is demo or not
        if (!key.equalsIgnoreCase(DKey)){
            VerifyDigitalSign vds = new VerifyDigitalSign();
            if (verifyLis() && vds.verify("MyData/SignedData.txt", "MyKeys/publicKey")){
            /*
            TODO start app in full mode
             */
                System.out.println("App running in FULL mode....");
            }else {
                infoBox("Failed Activation, Please check with Support for more info !","ERROR");
                System.out.println("App starting in Demo Mode...");
                /*
                TODO Start App in Demo mode
                 */
            }
        }else {
            System.out.println("Key identified as demo key hence , Starting App in Demo mode...");
            /*
            TODO Start App in Demo mode
             */
        }
    }

}

/*** =====================================FLOW to IMPLEMENT=================================================
 *  Check if activation flag is true
 *  If true
 *      KeyGeneration (Asymmetric)
 *      MID value and encryption made and keys stored in specific locations
 *      Set Act Flag to Default false again.
 *      Request user to send over 2 files for Activation key
 *  Else (False)
 *  If Key is Demo Key then Start App in demo mode
 *          Else
 *              Verify license with vds and verifyLis methods created already in this class
 *                  if Verify is true then Start App in full mode
 *                      else THROW ERROR "ACTIVATION FAILED, CONTACT ADMIN"
 */