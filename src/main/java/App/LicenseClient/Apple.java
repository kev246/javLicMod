package App.LicenseClient;


import java.io.File;
import java.io.FileWriter;
import java.util.Properties;


/***
 * This class is the main runner class which runs the whole flow of the licensing module.
 */
public class Apple extends Base {


    private final static String DKey = "WEFCDPE34432FWASOFNWEFOI32984SDFJOWER";

    /***
     * On this main method is where always the functional flow starts and depending upon the current
     * state of each cycle of execution/ cycle / file existence , the integration of classes helps in creating
     * the required functional flow of the Application.
     * @param args value
     * @throws Exception if the required files are not available
     */
    public static void main(String[] args) throws Exception {
        /* If case (with flags)
     new RequestLic();
     new ActKey();*/
        infoBox("Looking for available key information...","INFO:");
        if (!flaggerValueForActivation()){
            activationPrompt();
        }
        /* Else Case (flags )*/
        Properties Pn;
        Pn = loadPropertyFile("KAEncrypted.properties");
        String key = Pn.getProperty("KV");
        /* Checking key is demo or not */
        if (!key.equalsIgnoreCase(DKey)){
            Verifications vds = new Verifications();
            if (vds.verifyPnPmData()){
                if (vds.verify("MyData/SignedData.txt", "MyKeys/publicKey")){
                    if (vds.verifyK()){
                    /*
                    TODO start app in full mode
                    */
                        try {
                            if(infoBoxWithoutButton("FULL MODE ACTIVATED\n            Key is valid","INFO:")==0) {
                                String path = System.getProperty("user.dir");
                                String command = System.getenv("PYTHON_HOME").replace("\\", "/") + "/python " + path + "\\Host\\IntPy.pyo " + generatedString();
                                System.out.println(command);
                                Process p = Runtime.getRuntime().exec(command);
                                //infoBox("FULL MODE ACTIVATED\n            Key is valid","INFO:");
                                System.out.println("App running in FULL mode....");
                                makeflaggerActValueTrue();
                            }
                        }
                        catch (Exception e)
                        {
                            infoBox("Declare the PYTHON_HOME","Error");

                        }
                        finally {
                            Thread.sleep(15000);
                            File file=new File("./abc/Iaguikey.txt");
                            if(file.exists())
                                file.delete();
                        }
                       /* infoBox("FULL MODE ACTIVATED\n            Key is valid","INFO:");
                        System.out.println("App running in FULL mode....");
                        makeflaggerActValueTrue();*/
                    }else {
                        infoBox("Failed Activation, Please check with Support for more info !" +
                                "\n                DEMO MODE ACTIVATED","ERROR");
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
                Properties pnEnc = new Properties();
                pnEnc.setProperty("KV","EEEEYYYYYOOOOOPPPPNNNNVVVVXXXXJJJJFF");
                pnEnc.store(new FileWriter("KAEncrypted.properties"),"changed kv value..check lg");
                System.out.println("wrong key inputted so pnEnc check failed hence making kv value to unknown data ");
                makeFlaggerActValueFalse();
                infoBox("Key identified is neither demo nor Full Version Key\n Please check with Support team !","ERROR");
                /*
                TODO Start App in Demo mode
                 */
            }
        }else {
            System.out.println("Key identified as demo key hence , Starting App in Demo mode...");
            infoBox("DEMO MODE ACTIVATED\n" +
                    "Key identified is Demo Key","INFO:");
            requestPrompt();
            /*
            TODO Start App in Demo mode
             */
        }
    }

}

