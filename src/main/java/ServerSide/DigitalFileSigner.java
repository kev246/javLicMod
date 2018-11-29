package ServerSide;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

     class DigitalFileSigner {
        private List<byte[]> list;

        //The constructor of DigitalFileSigner class builds the list that will be written to the file.
        //The list consists of the message and the signature.
        private DigitalFileSigner(String data, String keyFile) throws Exception {
            list = new ArrayList<>();
            list.add(data.getBytes());
            list.add(sign(data, keyFile));
        }

        //The method that signs the data using the private key that is stored in keyFile path
        private byte[] sign(String data, String keyFile) throws Exception{
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(getPrivate(keyFile));
            rsa.update(data.getBytes());
            return rsa.sign();
        }

        //Method to retrieve the Private Key from a file
        private PrivateKey getPrivate(String filename) throws Exception {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }

        //Method to write the List of byte[] to a file
        private void writeToFile() throws IOException {
            File f = new File("MyData/SignedData.txt");
            f.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("MyData/SignedData.txt"));
            out.writeObject(list);
            out.close();
            System.out.println("Your file is ready.");
        }


        public static void main(String[] args) throws Exception{
            String data = JOptionPane.showInputDialog("Type your message here");
            new DigitalFileSigner(data, "src/main/resources/privateKey").writeToFile();
        }
    }