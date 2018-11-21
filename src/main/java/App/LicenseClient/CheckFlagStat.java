package App.LicenseClient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class CheckFlagStat extends Base{

    /**
     * Methods to access and reset flags for checks are designed below
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
