package App.LicenseClient;


import java.net.URL;
import java.net.URLClassLoader;
import java.security.cert.Certificate;
import java.util.Base64;

public class licenseCheck {

    /***
     * TODO implement digital sign validation
     * TODO Obfuscate this class file
     */

    private final String cert
            = "MIIDLzCCAhegAwIBAgIESMPzJjANBgkqhkiG9w0BAQsFADBIMQowCAYDVQQGEwEx";
    public final String LiPath = System.getProperty("user.dir") + "\\src\\main\\java\\";
    public final LicenseInfo checkLicense(String path) {
        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file://" + path)});
            Class licenseClass = urlClassLoader.loadClass("Liscense.License");
            /*Certificate[] certs = licenseClass.getProtectionDomain().getCodeSource().getCertificates();
            if (certs == null || certs.length > 1) {
                throw new RuntimeException("No certificates");
            }
            if (!(Base64.getEncoder().encodeToString(certs[0].getEncoded()).equals(cert))) {
                throw new RuntimeException("Invalid cert");
            }*/
            LicenseInfo licenseInfo = new LicenseInfo();
            Object license = licenseClass.newInstance();
            licenseInfo.setId((String) licenseClass.getDeclaredMethod("getId", null).invoke(license, null));
            licenseInfo.setEmail((String) licenseClass.getDeclaredMethod("getEmail", null).invoke(license, null));
            licenseInfo.setCompany((String) licenseClass.getDeclaredMethod("getCompany", null).invoke(license, null));
            return licenseInfo;
        } catch (Exception ex) {
            throw new RuntimeException("Invalid license");
        }

    }
}
