package App.LicenseClient;

import java.util.Properties;


public class Apple extends Base {

    private final static String DKey = "WEFCDPE34432FWASOFNWEFOI32984SDFJOWER";


    public static void main(String[] args) throws Exception {
        //Checking if valid request is present
  /*      new RequestLic();
        new ActKey();*/
        infoBox("Looking for available key information...","INFO:");
        if (!flaggerValueForActivation()){
            activationPrompt();
        }
        //Else Case
        Properties Pn;
        Pn = loadPropertyFile("KAEncrypted.properties");
        String key = Pn.getProperty("KV");
        //Checking key is demo or not
        if (!key.equalsIgnoreCase(DKey)){
            VerifyDigitalSign vds = new VerifyDigitalSign();
            if (vds.verifyPnPmData()){
                if (vds.verify("MyData/SignedData.txt", "MyKeys/publicKey")){
                    if (vds.verifyK()){
                    /*
                    TODO start app in full mode
                    */
                        infoBox("FULL MODE ACTIVATED\n            Key is valid","INFO:");
                        System.out.println("App running in FULL mode....");
                        makeflaggerActValueTrue();
                    }else {
                        infoBox("Failed Activation, Please check with Support for more info !\n                DEMO MODE ACTIVATED","ERROR");
                        System.out.println("App starting in Demo Mode...");
                /*
                TODO Start App in Demo mode
                 */
                    }
                }else{
                    infoBox("Failed Digital Sign Verification, Please check with support for more info !","ERROR");
                }

            }else {
                infoBox("Failed Activation, Please check with Support for more info !","ERROR");
                System.out.println("App starting in Demo Mode...");
                /*
                TODO Start App in Demo mode
                 */
            }
        }else {
            System.out.println("Key identified as demo key hence , Starting App in Demo mode...");
            infoBox("DEMO MODE ACTIVATED\nKey identified is Demo Key","INFO:");
            requestPrompt();
            /*
            TODO Start App in Demo mode
             */
        }
    }

}

