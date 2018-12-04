package App.LicenseClient;

     import oshi.SystemInfo;
     import oshi.hardware.*;
     import oshi.software.os.OperatingSystem;

     import java.io.File;
     import java.io.FileNotFoundException;
     import java.io.FileWriter;
     import java.io.IOException;
     import java.security.GeneralSecurityException;
     import java.util.Properties;
     import java.util.Scanner;

/***
 * ComputerIdentifier class is the integration class which utilizes the external open API which helps getting all
 * required information of the machine and creating the Machine ID in the required format
 */
class ComputerIdentifier
     {
         /***
          * ComputerIdentifier constructor helps calling the createMidFile which internally creates the Property file for machine ID
          * @throws GeneralSecurityException
          * @throws IOException
          */
          ComputerIdentifier() throws GeneralSecurityException, IOException {
               createMidFile();
          }

         /***
          * createMid method helps create the machine id of any usable system /machine using the API calls from external
          * library which helps return all required kinds of system information independent of OS or machine flavor.
          * @return machine ID
          */
         private static String createMid()
     {
          String diskSerialID="";
          String macAdd = "";
     SystemInfo systemInfo = new SystemInfo();
     OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
     HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
     CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
     ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
          HWDiskStore[]  HWDStore = hardwareAbstractionLayer.getDiskStores(); //for first disk serial number from list of disc if any
          for(int x = 0; x <HWDStore.length; x++) {
               diskSerialID = HWDStore[0].getSerial().trim();
          }

          NetworkIF[] net = hardwareAbstractionLayer.getNetworkIFs(); // for first mac address from list
          if (net.length > 0){
               for (int y = 0; y < net.length; y++){
                    macAdd = net[0].getMacaddr().trim();
               }
          }

     String vendor = operatingSystem.getManufacturer();
     String processorSerialNumber = computerSystem.getSerialNumber(); //processor serialNumber is Bios serialNumber
     String processorIdentifier = centralProcessor.getProcessorID();  //processorID is same as CPUID
     String motherBoardIdentifier = computerSystem.getBaseboard().getSerialNumber();
     int processors = centralProcessor.getLogicalProcessorCount(); //No of cores like logical cpu count for this machine

     String delimiter = "#";

     return vendor +
     delimiter +
     processorSerialNumber +
     delimiter +
     processorIdentifier +
     delimiter +
     processors +
     delimiter +
     motherBoardIdentifier + delimiter + diskSerialID + delimiter + macAdd;
     }

         /***
          * readKeyFile method here helps just read the key value from the key file as plane string
          * @param keyFile generated from symmetric encryption technique
          * @return String value of KeyFile
          * @throws FileNotFoundException if key file not found
          */
     private static String readKeyFile(File keyFile) throws FileNotFoundException {
          Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
          String keyValue = scanner.next();
          scanner.close();
          return keyValue;
          }

         /***
          * halfLengthData method is designed to create an extract string data from the resultant machine ID created using
          * createMid method
          * @return new code string of machine ID
          */
          static String halfLengthData(){
              String uid = createMid();
              String result = uid.replaceAll("[#:/]", "");
              int length = result.length();
              int x = length/2;
              result = result.substring(x, length);
              return result;
     }

         /***
          * createMidFile method helps create the file which stores the encrypted data of Mid created from same class method createMid
          * and store in specific file location.
          * @throws GeneralSecurityException if file not found
          * @throws IOException if file is missing or not created
          */
         private void createMidFile() throws GeneralSecurityException, IOException {

          String uId = createMid();
          final String resourcePath = System.getProperty("user.dir")+"\\src\\main\\resources\\";
          String keypath = System.getProperty("user.dir")+"\\abc\\";
          final String PNEW_KEY = keypath+"NewKeyFile.key";
          final String PNEW_ENCFILE = resourcePath+"PmEnc.properties";

          if (!uId.equals("")) {
               Properties pn = new Properties();
               String PnEnc = SymEncPM.encrypt(uId, new File(PNEW_KEY));
               pn.put("MID", PnEnc);
               String keyValue = readKeyFile(new File(PNEW_KEY));
               pn.put("PID", keyValue);
               pn.store(new FileWriter(PNEW_ENCFILE),"Pn created and ready to be send to server for license key activation process");

          }
          else{
               throw new RuntimeException("unable to find any system information");
          }
     }

       /*  public static void main(String[] args) {
             String x = createMid();
             System.out.println(x);
             System.out.println(halfLengthData());


         }*/
     }

