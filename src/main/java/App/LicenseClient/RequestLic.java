package App.LicenseClient;

import Asymmetric.SiblingKCreate;

import java.io.IOException;
import java.security.GeneralSecurityException;


/***
 *
 * This class helps in creating a request for license purchase for which we need to create Machine specific files and the Public /Private Keys
 * which will be used to sign and verify Digital Signature flow
 */
class RequestLic extends Base {

    /*
    TODO Make key also unique by modifying it (add mid into key value at the end ,to make a new string not enc but string and then use that as key file later on
     */

    /***
     * The constructor can be used if the requests for these actions within Request Action method needs to be triggered via Flag setting
     */
    RequestLic() {
        if (CheckFlagStat.checkFileForReqStat()) {
            System.out.println("request status is enabled");


        }else{
            System.out.println("No request for activation. Looking at available key info...");
        }
       }

    /***
     * RequestActions method does basically all required operations to initiate a request which includes
     * 1. creating asymmetric keys (public and private)
     * 2. creating machine ID and encrypting Symmetrically to make a data file
     * 3. Prompt box to user to send over the files to Tech M
     */
    protected static void RequestActions(){
           try {
               new ComputerIdentifier();
               SiblingKCreate gk = new SiblingKCreate(1024);
               gk.createKeys();
               gk.writeToFile("MyKeys/publicKey", gk.getPublicKey().getEncoded());
               gk.writeToFile("src/main/resources/privateKey", gk.getPrivateKey().getEncoded());
           } catch (GeneralSecurityException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               infoBox("Please Send over the 2 files at resources location to get Activation File.\n1. PmEnc \n2. privateKey","Action required !");
           }
       }

    }
