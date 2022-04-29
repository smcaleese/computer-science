import java.util.*;
import java.security.*;
import java.math.*;
import java.io.*;
import java.lang.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.*;
import java.nio.file.*;

public class Assignment1 implements Assignment1Interface {
    /* Method generateKey returns the key as an array of bytes and is generated from the given password and salt. */
    public byte[] generateKey(byte[] password, byte[] salt) {
        byte[] key = null;
        try {
            byte[] passwordAndSaltBytes = HelperMethods.mergeArrays(password, salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            key = md.digest(passwordAndSaltBytes); // hash once
            // hash 199 more times
            for(int i = 1; i < 200; i++) {
                key = md.digest(key);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return key;
    }

    /* Method encryptAES returns the AES encryption of the given plaintext as an array of bytes using the given iv and key */
    public byte[] encryptAES(byte[] plaintext, byte[] iv, byte[] key) {
        byte[] cipherText = null;
        try {
            Cipher AESCipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            AESCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            cipherText = AESCipher.doFinal(plaintext);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return cipherText;
    }

    public byte[] decryptAES(byte[] ciphertext, byte[] iv, byte[] key) {
        byte[] paddedPlaintext = null;
        try {
            Cipher AESCipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            AESCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            paddedPlaintext = AESCipher.doFinal(ciphertext);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        byte[] plaintextBytes = HelperMethods.removePadding(paddedPlaintext);
        return plaintextBytes;
    }

    /* Method encryptRSA returns the encryption of the given plaintext using the given encryption exponent and modulus */
    public byte[] encryptRSA(byte[] plaintext, BigInteger exponent, BigInteger modulus) {
        BigInteger p = new BigInteger(plaintext);
        BigInteger modExp = this.modExp(p, exponent, modulus);
        byte[] encryption = modExp.toByteArray();
        return encryption;
    }

    /* Method modExp returns the result of raising the given base to the power of the given exponent using the given modulus */
    public BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulus) {
        // left to right variant of square and multiply algorithm
        String binExponent = exponent.toString(2);
        int k = binExponent.length();
        BigInteger y = new BigInteger("1", 10);
        for(int i = 0; i < binExponent.length(); i++) {
            y = y.multiply(y).mod(modulus);
            if(binExponent.charAt(i) == '1') {
                y = y.multiply(base).mod(modulus);
            }
        }
        return y;
    }

    public static void main(String[] args) {
        // command to run this program: javac Assignment1.java && java Assignment1 Assignment1.class > Encryption.txt
        try {
            Assignment1 program = new Assignment1();

            // 1. encrypt the password with RSA using the public exponent and public modulus
            String password = "X)Lmv!nux6A9G5%";
            byte[] passwordBytes = password.getBytes("UTF-8");

            BigInteger e = new BigInteger("65537", 10);
            // 1.1 this is the public modulus from the assignment specification:
            String publicModulus = "c406136c12640a665900a9df4df63a84fc855927b729a3a106fb3f379e8e4190" +
            "ebba442f67b93402e535b18a5777e6490e67dbee954bb02175e43b6481e7563d" +
            "3f9ff338f07950d1553ee6c343d3f8148f71b4d2df8da7efb39f846ac07c8652" +
            "01fbb35ea4d71dc5f858d9d41aaa856d50dc2d2732582f80e7d38c32aba87ba9";
            BigInteger N = new BigInteger(publicModulus, 16);

            byte[] encryptedPasswordBytes = program.encryptRSA(passwordBytes, e, N);

            // 2. convert the password to hexadecimal and output it to Password.txt
            String encryptedPasswordHexString = HelperMethods.encodeHexString(encryptedPasswordBytes);
            HelperMethods.outputStringToFile(encryptedPasswordHexString, "Password.txt");

            // 3. read the encrypted password from Password.txt and decrypt it using the private exponent
            String encryptedPasswordHex = HelperMethods.readFile("Password.txt");
            byte[] encryptedPasswordBytesFromFile = HelperMethods.decodeHexString(encryptedPasswordHex);

            // 3.1 add the privateExponent that corresponds to the public modulus above here:
            String privateExponent = "";
            BigInteger d = new BigInteger(privateExponent, 16);

            byte[] decryptedPasswordBytes = program.encryptRSA(encryptedPasswordBytesFromFile, d, N);
//            byte[] decryptedPasswordBytes = passwordBytes;

            // 4. use the password and the salt to generate the AES key
            String salt = HelperMethods.readFile("Salt.txt");
            byte[] saltBytes = HelperMethods.decodeHexString(salt);
            byte[] AESKey = program.generateKey(decryptedPasswordBytes, saltBytes);

            //  5. encrypt the class file using the AES key and output the result to Encryption.txt
            String IVString = HelperMethods.readFile("IV.txt");
            byte[] IVBytes = HelperMethods.decodeHexString(IVString);

            // 5.1 read the Assignment.class bytes into a byte array
            String classFileName = args[0];
            byte[] plaintextBytes = Files.readAllBytes(Paths.get(classFileName));

            // 5.2 pad the plaintext
            byte[] paddedPlaintextBytes = HelperMethods.applyPaddingToPlainText(plaintextBytes);

            // 5.3 encrypt the plaintext using the AES cipher
            byte[] aesCipherBytes = program.encryptAES(paddedPlaintextBytes, IVBytes, AESKey);

            //  5.4 convert the encrypted bytes to a hex string and output it to Encryption.txt
            String aesCipherString = HelperMethods.encodeHexString(aesCipherBytes);

            // 5.5 output to Encryption.txt
            System.out.print(aesCipherString);

            // 6. read the encrypted hexadecimal from Encryption.txt and convert it to bytes
            String encryptedHex = HelperMethods.readFile("Encryption.txt");
            byte[] encryptedBytes = HelperMethods.decodeHexString(encryptedHex);

            // 7. decrypt the encrypted bytes and output the decrypted bytes to DecryptedOutput.class
            byte[] decryptedBytes = program.decryptAES(encryptedBytes, IVBytes, AESKey);
            FileOutputStream binaryWriter = new FileOutputStream("DecryptedOutput.class");
            binaryWriter.write(decryptedBytes);

            // 8. check DecryptedOutput.class to make sure it decompiles correctly and looks the same as Assignment1.class

        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}