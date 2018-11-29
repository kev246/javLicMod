package App.LicenseClient;

import Asymmetric.SiblingKCreate;

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
        if (CheckFlagStat.checkFileForReqStat()) {
            System.out.println("request status is enabled");


        }else{
            System.out.println("No request for activation. Looking at available key info...");
        }
       }

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
