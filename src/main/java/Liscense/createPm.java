package Liscense;

import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;

public class createPm {

    /*
    Write a program to retrieve motherboardID for any system (Windows/Linux/Mac) and
    using that create new unique value for Pm and then using symmetric encryption encrypt the same
     */

    public static String getSystemMotherBoard_SerialNumber(){

        try{
            String OSName=  System.getProperty("os.name");
            if(OSName.contains("Windows")){
                return (getWindowsMotherboard_SerialNumber());
            }
            else{
                return (GetLinuxMotherBoard_serialNumber());
            }
        }
        catch(Exception E){
            System.err.println("System MotherBoard Exp : "+E.getMessage());
            return null;
        }
    }

    /***
     * Method for byte array to hex string and vice versa conversion
     */
    private static String byteArrayToHString(byte[] b){
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++){
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }


    /**
     * Method for get Windows Machine MotherBoard Serial Number
     * @return
     */
    private static String getWindowsMotherboard_SerialNumber() {
        String result = "";

        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs =
                    "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                            + "Set colItems = objWMIService.ExecQuery _ \n"
                            + "   (\"Select * from Win32_BaseBoard\") \n"
                            + "For Each objItem in colItems \n"
                            + "    Wscript.Echo objItem.SerialNumber \n"
                            + "    exit for  ' do the first cpu only! \n"
                            + "Next \n";

            fw.write(vbs);
            fw.close();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
           // System.out.println(file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception E){
            System.err.println("Windows MotherBoard Exp : "+E.getMessage());
        }
        return result.trim();
    }

    public static String getDiscSerialNumber(){
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * Method for get Linux Machine MotherBoard Serial Number
     * @return
     */
    private static String GetLinuxMotherBoard_serialNumber() {
        String command = "dmidecode -s baseboard-serial-number";
        String sNum = null;
        try {
            Process SerNumProcess = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));
            sNum = sNumReader.readLine().trim();
            SerNumProcess.waitFor();
            sNumReader.close();
        }
        catch (Exception ex) {
            System.err.println("Linux Motherboard Exp : "+ex.getMessage());
            sNum =null;
        }
        return sNum;
    }

    private static String getWinBios_serialNumberViaCmd(){
        String sVal = "";
        try {
            Process p1 = Runtime.getRuntime().exec("wmic bios get serialNumber");
            BufferedReader input = new BufferedReader(new InputStreamReader(p1.getInputStream()));
            String line;


            while((line = input.readLine()) != null){
                if (line.startsWith("SerialNumber")) {
                    line = line.replace("SerialNumber", "").trim();
                }
                sVal += line;

            }
            input.close();
        } catch (Exception ex) {
            System.out.println("Win MID couldn't be obtained via cmd or vbs ways " + ex.getMessage());
            sVal = null;
        }
        return sVal.trim();
    }

    public static String getMachineID () {
        String value ;
        String motherBoard_SerialNumber = getSystemMotherBoard_SerialNumber();
        if (motherBoard_SerialNumber == null || motherBoard_SerialNumber.equalsIgnoreCase("")) {
            String biosID = getWinBios_serialNumberViaCmd();
            System.out.println("since cant find MID finding BID for this machine....");
            value = biosID;
        }
        else
        {
            value = motherBoard_SerialNumber;
        }

        return value;
    }


    /*
    Create Pm (property file) or run this class only if there is a request for activation.
     */
    public static void main(String[] args) throws GeneralSecurityException, IOException {
         String uId = ComputerIdentifier.generateLicenseKey();
        final String PNEW_KEY = "NewKeyFile.key";
        final String PNEW_ENCFILE = "PnewEnc.properties";
        if (uId != null) {
            Properties pn = new Properties();
            String PnEnc = encDecrExample.encrypt(uId, new File(PNEW_KEY));
            pn.put("MID", PnEnc);
            pn.store(new FileWriter(PNEW_ENCFILE),"Pn created and ready to be send to server for license key activation process");

        }
        else{
            throw new RuntimeException("unable to find system info");
        }

        }
    }



