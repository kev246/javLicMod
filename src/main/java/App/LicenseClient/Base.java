package App.LicenseClient;

import javax.swing.*;
import java.io.*;
import java.util.Objects;
import java.util.Properties;

 class Base {
    protected static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

     protected static int yesNoBox(String infoMessage, String titleBar)
     {
         int x = JOptionPane.showConfirmDialog(null, infoMessage, titleBar,1, 3);
         return x;
     }
    protected static Properties loadPropertyFile(String fileName){
        Properties p1 = new Properties();
        try {
            p1.load(new FileReader(fileName));
        } catch (IOException e) {
            infoBox("Required File missing or not created.", "ERROR:");
            e.printStackTrace();
        }
        return p1;
    }
     protected static void makeflaggerActValueTrue(){
        Properties p ;
         p = loadPropertyFile("./src/main/resources/flagger.properties");
         p.setProperty("ActKeyStat", "true");
         try {
             p.store(new FileWriter("src/main/resources/flagger.properties"),"value changed...");
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
     protected static void makeflaggerActValueFalse(){
         Properties p;
         p = loadPropertyFile("./src/main/resources/flagger.properties");
         p.setProperty("ActKeyStat", "false");
         try {
             p.store(new FileWriter("src/main/resources/flagger.properties"),"value changed to false...");
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     protected static boolean flaggerValueForActivation(){
         Properties p1 = loadPropertyFile("./src/main/resources/flagger.properties");
         boolean vals = "true".equals(p1.getProperty("ActKeyStat"));
         if (vals){
             return true;
         }else {
             return false;
         }
     }

     protected static void requestPrompt(){
         if (checkIfFileExists("./src/main/resources/privateKey")) {
             System.out.println("file available");
             System.exit(1);
         }else {
             System.out.println("file not found");
             int val = yesNoBox("Please request for activation to access full mode??\n                          Request Now ???","User Confirmation");
             switch(val)
             {
                 case 0:
                     RequestLic.RequestActions();
                     break;
                 case 1:
                     break;
                 case 3:
                     break;
         }

     }}
     protected static void activationPrompt(){
         if (checkIfFileExists("./MyData/SignedData.txt")) {
             System.out.println("file found");
                if (!flaggerValueForActivation()) {
                    int val = yesNoBox("Do you want to activate key ?", "User Confirmation");
                    switch (val) {
                        case 0:
                            ActKey.startAct();
                            makeflaggerActValueTrue();
                            break;
                        case 1:
                            break;
                        case 3:
                            break;

                    }
                }
         }else {
                 System.out.println("digital file not available");
                 makeflaggerActValueFalse();
         }

     }



     static boolean checkIfFileExists(String fileName) {
         boolean found = false;
         try {
             File file = new File(fileName);
             if (file.exists() && file.isFile())
             {
                 found = true;
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         return found;
     }

    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        }
        finally {
            br.close();
        }
    }

}
