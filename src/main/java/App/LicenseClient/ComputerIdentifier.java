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

class ComputerIdentifier
     {
          ComputerIdentifier() throws GeneralSecurityException, IOException {
               createMidFile();
          }
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
                    macAdd = net[2].getMacaddr().trim();
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

     private static String readKeyFile(File keyFile) throws FileNotFoundException {
          Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
          String keyValue = scanner.next();
          scanner.close();
          return keyValue;
          }

          static String halfLengthData(){
              String uid = createMid();
              String result = uid.replaceAll("[#:/]", "");
              int length = result.length();
              int x = length/2;
              result = result.substring(x, length);
              return result;
     }


         void createMidFile() throws GeneralSecurityException, IOException {

          String uId = createMid();
          final String resourcePath = System.getProperty("user.dir")+"\\src\\main\\resources\\";
          String keypath = System.getProperty("user.dir")+"\\abc\\";
          final String PNEW_KEY = keypath+"NewKeyFile.key";
          final String PNEW_ENCFILE = resourcePath+"PmEnc.properties";

          if (uId != "") {
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

