package App.LicenseClient;

import javax.swing.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;

/***
 * Base class
 * This class contains all the reusable methods which can come handy in many classes for easy code flow,
 * and hence this class will be extended to most classes as parent class.
 */

class Base {
    /***
     *infoBox Method to show information box as a pop up window or alert window about current flow state
     * @param infoMessage string input which is the main message to the user
     * @param titleBar string is the title for the dialog box
     */
    protected static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /***
     * yesNoBox Method to show a Alert or input window thrown on to the user for collecting user input about deciding which flow to take
     * further depending upon user input
     * @param infoMessage string input which is the main message to the user
     * @param titleBar string is the title for the dialog box
     * @return boolean
     */
     protected static int yesNoBox(String infoMessage, String titleBar)
     {
         int x = JOptionPane.showConfirmDialog(null, infoMessage, titleBar,1, 3);
         return x;
     }

     /*
     loadPropertyFile Method to load a property file and return property file , so that this can be reused in many other classes
      */
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
    /*
     makeflaggerActValueTrue Method to make flag value False for using it to make the Application flow conditional one
      */
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
     /*
     makeFlaggerActValueFalse Method to make flag value False for using it to make the Application flow conditional one
      */
     protected static void makeFlaggerActValueFalse(){
         Properties p = loadPropertyFile("./src/main/resources/flagger.properties");
         p.setProperty("ActKeyStat", "false");
         try {
             p.store(new FileWriter("src/main/resources/flagger.properties"),"value changed to false...");
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


     /*
     flaggerValueForActivation Method to check the current value of ActKeyStat key inside the flagger property file,
     while in any flow of the Application.
     Initially set to false always.
      */

     protected static boolean flaggerValueForActivation(){
         Properties p1 = loadPropertyFile("./src/main/resources/flagger.properties");
         boolean vals = "true".equals(p1.getProperty("ActKeyStat"));
         return vals;
     }

    /***
     * Request Prompt method helps with triggering the Request Action main method within the flow depending upon
     * file existence on specific path ie;
     * This method checks if file which is generated on request is already present in specific location or not
     * if available its skipping the request action (meaning this is already requested once by user)
     * else not available that means user has never requested for licensing and hence prompting user with a Decision question
     * to weather you need to request or not until at least once requested.
     */
    protected static void requestPrompt(){
         if (checkIfFileExists("./src/main/resources/privateKey") && checkIfFileExists("./src/main/resources/PmEnc.properties")) {
             System.out.println("file available meaning a;requested already once");
             System.exit(1);
         }else {
             System.out.println("file not found meaning not requested yet");
             int val = yesNoBox("Please request for activation to access full mode!\n                          Request Now ???","User Confirmation");
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

     }
    }

    /***
     * activationPrompt method helps with triggering the StartAct main method (trigger for starting activation when key and digital
     * signed file is received at user end after requesting is once done and file are available
     * This prompt of Decision making is thrown only to user screen for inputs when the files required for activation is ready at
     * host side (signed file, Activation Key) and should stop prompting once the activation is made by user successfully.
     */
     protected static void activationPrompt(){
         if (checkIfFileExists("./MyData/SignedData.txt")) {
             System.out.println("file found");
                if (!flaggerValueForActivation()) {
                    int val = yesNoBox("Do you want to activate key ?", "User Confirmation");
                    switch (val) {
                        case 0:
                            ActKey.startAct();

                            break;
                        case 1:
                            break;
                        case 3:
                            break;

                    }
                }
         }else {
                 System.out.println("digital file not available");
                 makeFlaggerActValueFalse();
         }

     }

    /***
     * checkIfFileExists Method call helps in checking if a specific file is available in a given path
     * @param fileName any file to check the existence of the file
     * @return yes or no
     */

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

     /*
     readFile method helps in reading contents within a file line by line and storing it as string and returning the same.
      */
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
    static String generatedString(){
        String ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,";
        int keylength=10;
        Random random=new Random();
        String str;
        StringBuilder builder=new StringBuilder(keylength);
        for(int i=0;i<keylength;i++)
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        try
        {
            File file;
            FileWriter writer=new FileWriter("./abc/Iaguikey.txt",false);
            writer.write(builder.toString());
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }
    protected static int infoBoxWithoutButton(String infoMessage, String titleBar)
    {
        int input = JOptionPane.showOptionDialog(null, infoMessage, titleBar, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        return input;
    }


}
