package Liscense;

     import oshi.SystemInfo;
     import oshi.hardware.CentralProcessor;
     import oshi.hardware.ComputerSystem;
     import oshi.hardware.HWDiskStore;
     import oshi.hardware.HardwareAbstractionLayer;
     import oshi.software.os.OperatingSystem;

     class ComputerIdentifier
     {
     static String generateLicenseKey()
     {
          String diskSerialID="";
     SystemInfo systemInfo = new SystemInfo();
     OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
     HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
     CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
     ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
          HWDiskStore[]  HWDStore = hardwareAbstractionLayer.getDiskStores();
          for(HWDiskStore disk : HWDStore) {
               diskSerialID = disk.getSerial().trim();
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
     motherBoardIdentifier + delimiter + diskSerialID;
     }

     public static void main(String[] arguments)
     {
     String identifier = generateLicenseKey();
     System.out.println(identifier);
     }
     }

