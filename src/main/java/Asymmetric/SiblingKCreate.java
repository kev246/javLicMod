package Asymmetric;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

/***
 * SiblingKCreate class helps in creating Private and public keys for asymmetric encryption or digital signing procedure.
 * It uses the RSA algorithm for creating sibling keys
 */
public class SiblingKCreate {

        private KeyPairGenerator keyGen;
        private KeyPair pair;
        private PrivateKey privateKey;
        private PublicKey publicKey;

    /**
     * SiblingKCreate class constructor takes key length as an argument to initialize the KeyPairGenerator
     * @param keylength as input of what bytecode to choose 128 or 256 for encryption
     * @throws NoSuchAlgorithmException if mentioned algorithm is missing
     * @throws NoSuchProviderException if the provider is missing
     */
        public SiblingKCreate(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {

            this.keyGen = KeyPairGenerator.getInstance("RSA");
            this.keyGen.initialize(keylength);
        }

    /**
     * createKeys method helps in creating the key pair from the Keygen instance created at constructor
      */
    public void createKeys() {
            this.pair = this.keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();

        }

    /**
     * GetPrivateKey method helps in getting privateKey for saving as file after creating the same
     * @return privateKey info
     */
        public PrivateKey getPrivateKey() {
            return this.privateKey;
        }

    /**
     * GetPublicKey method helps in getting publicKey for saving as file after creating the same
     * @return publicKey info
     */
        public PublicKey getPublicKey() {
            return this.publicKey;
        }

    /**
     * writeToFile method helps in writing and storing the acquired data based on input parameters of path and
     * byte array of key information which is not encoded
     * @param path as the file path
     * @param key as byte array information
     * @throws IOException
     */
        public void writeToFile(String path, byte[] key) throws IOException {

            File f = new File(path);
            f.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(key);
            fos.flush();
            fos.close();
        }

      /*  public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
            SiblingKCreate myKeys = new SiblingKCreate(1024);
            myKeys.createKeys();
            myKeys.writeToFile("MyKeys/publicKey", myKeys.getPublicKey().getEncoded());
            myKeys.writeToFile("src/main/resources/privateKey", myKeys.getPrivateKey().getEncoded());
        }*/
    }
