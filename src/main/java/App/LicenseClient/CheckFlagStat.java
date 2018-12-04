package App.LicenseClient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


/**
 * CheckFlagStat class was designed to help check the status of all flags at any instance of the functional flow to trigger actions
 * based on the flag status
 */
class CheckFlagStat extends Base{

    /**
     * Method checkFileForReqStat to access and reset flags for checks and accordingly switch the action in functional flow
     * @return boolean
     */

    static boolean checkFileForReqStat(){

        Properties p;
        p = loadPropertyFile(System.getProperty("user.dir")+"\\src\\main\\resources\\flagger.properties");
        boolean val = "true".equals(p.getProperty("ReqStat")) ;
        if(val){
            p.setProperty("ReqStat", "false");
            try {
                p.store(new FileWriter("src/main/resources/flagger.properties"),"value restored...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    return val;
    }


    /**
     * Method checkFileForActKeyStat to access and reset flags for checks and accordingly switch the action in functional flow
     * @return boolean yes or no
     */

    static boolean checkFileForActKeyStat(){

        Properties p;
        p = loadPropertyFile("src/main/resources/flagger.properties");
        boolean vals = "true".equals(p.getProperty("ActKeyStat")) ;
        if(vals){
            p.setProperty("ActKeyStat", "false");
            try {
                p.store(new FileWriter("src/main/resources/flagger.properties"),"value restored...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return vals;
    }

    }
