package App.LicenseClient;

public class requestLcs {

    /*
    TODO Write logic to create PM (Encrypted) from PD (default prop) using machine motherboard ID
        m1: obtain motherboardID
        m2: Creating PM ===>>
        multiply mid with current PID encrypt data and now encrypt that string with 2 property LiscenceID: and MID into a property file(PM) created.
        Store it in special location as this will be later used to compared lis for comparing msg (no need to decrypt)
    TODO Send Pm to server side PM which contains Unique new Lis, MID, encrypted within PM.
    TODO Make key also unique by modifying it (add mid into key value at the end ,to make a new string not enc but string and then use that as key file later on
     */
}
