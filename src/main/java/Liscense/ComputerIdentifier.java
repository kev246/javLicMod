package Liscense;

     import oshi.SystemInfo;
     import oshi.hardware.*;
     import oshi.software.os.OperatingSystem;

     import java.io.File;
     import java.io.FileWriter;
     import java.io.IOException;
     import java.security.GeneralSecurityException;
     import java.util.Properties;

class ComputerIdentifier
     {
     private static String generateLicenseKey()
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

     public static void main(String[] arguments) throws GeneralSecurityException, IOException {
          String uId = generateLicenseKey();
          System.out.println("unique id is :\n"+uId);
          final String PNEW_KEY = "NewKeyFile.key";
          final String PNEW_ENCFILE = "PnewEnc.properties";
          if (uId != null) {
               Properties pn = new Properties();
               String PnEnc = encDecrExample.encrypt(uId, new File(PNEW_KEY));
               System.out.println("encrypted information is :\n"+PnEnc);
               pn.put("MID", PnEnc);
               pn.store(new FileWriter(PNEW_ENCFILE),"Pn created and ready to be send to server for license key activation process");

          }
          else{
               throw new RuntimeException("unable to find system info");
          }
     }
     }

