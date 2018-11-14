package ServerSide;

import App.LicenseClient.Base;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LiscenceKeyGenerator extends Base {

    /***
     *
     * @param Mid
     * @return key created out of unique machine ID created and encrypted , send over, first to be decrypted to get Mid
     */
    static String halfLengthData(String Mid) {
        String result = Mid.replaceAll("[#:/]", "");
        int length = result.length();
        int x = length / 2;
        result = result.substring(x, length);
        return result;
    }


    public static void main(String[] args) {
        try {
            String MID = DecryptPM.decryptData();
            String Output = halfLengthData(MID);
            infoBox(Output,"Unique Key generated:\n");

        } catch (IOException e) {
            e.printStackTrace();

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
