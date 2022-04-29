import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import javax.crypto.*;
import java.security.*;
import java.math.*;
import java.nio.*;
import java.nio.file.*;

public class Tests {
    @Test
    public void testUTF8PasswordToByteArray1() {
        try {
            String password = "1234";
            byte[] passwordBytes = password.getBytes("UTF-8");
            byte[] expectedPasswordBytes = {49, 50, 51, 52};
            assertArrayEquals(passwordBytes, expectedPasswordBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUTF8PasswordToByteArray2() {
        try {
            String password = "X)Lmv!nux6A9G5%";
            byte[] passwordBytes = password.getBytes("UTF-8");
            byte[] expectedPasswordBytes = {88, 41, 76, 109, 118, 33, 110, 117, 120, 54, 65, 57, 71, 53, 37};
            assertArrayEquals(passwordBytes, expectedPasswordBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hexStringToByteArray1() {
        String hexString = "05";
        byte[] expectedByteArray = {5};
        byte[] actualByteArray = HelperMethods.decodeHexString(hexString);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void hexStringToByteArray2() {
        String saltString = "4979536220664d2b2378385418736c05";
        byte[] expectedByteArray = {73, 121, 83, 98, 32, 102, 77, 43, 35, 120, 56, 84, 24, 115, 108, 5};
        byte[] actualByteArray = HelperMethods.decodeHexString(saltString);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testPasswordAndSaltMerge() {
        byte[] passwordBytes = {88, 41, 76, 109, 118, 33, 110, 117, 120, 54, 65, 57, 71, 53, 37};
        byte[] saltBytes = {73, 121, 83, 98, 32, 102, 77, 43, 35, 120, 56, 84, 24, 115, 108, 5};
        byte[] actualMergedArray = HelperMethods.mergeArrays(passwordBytes, saltBytes);
        byte[] expectedArray = {88, 41, 76, 109, 118, 33, 110, 117, 120, 54, 65, 57, 71, 53, 37, 73, 121, 83, 98, 32, 102, 77, 43, 35, 120, 56, 84, 24, 115, 108, 5};
        assertArrayEquals(actualMergedArray, expectedArray);
    }

    @Test
    public void testAESKeyGeneration() {
        try {
            byte[] passwordAndSaltBytes = {88, 41, 76, 109, 118, 33, 110, 117, 120, 54, 65, 57, 71, 53, 37, 73, 121, 83, 98, 32, 102, 77, 43, 35, 120, 56, 84, 24, 115, 108, 5};
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] expectedKey = md.digest(passwordAndSaltBytes);
            for(int i = 1; i < 200; i++) {
                expectedKey = md.digest(expectedKey);
            }
            byte[] password = {88, 41, 76, 109, 118, 33, 110, 117, 120, 54, 65, 57, 71, 53, 37};
            byte[] salt = {73, 121, 83, 98, 32, 102, 77, 43, 35, 120, 56, 84, 24, 115, 108, 5};
            byte[] actualKey = new Assignment1().generateKey(password, salt);
            assertArrayEquals(expectedKey, actualKey);
        } catch(Exception e) {
           e.printStackTrace();
        }
    }

    @Test
    public void testByteArrayToBitString() {
        // convert a byte array to a binary string and back into a byte array
        byte[] plaintextBytes = {1, 2, -3, -5, -2, 86, 43, 7};
        String bitString = HelperMethods.byteArrayToBitString(plaintextBytes);
        byte[] newBytesArr = HelperMethods.binaryStringToByteArray(bitString);
        assertArrayEquals(plaintextBytes, newBytesArr);
    }

    @Test
    public void testPadding1() {
        // plaintext that is shorter than a single block
        byte[] plaintextBytes = {1, 2, 3};
        byte[] paddedBytes = HelperMethods.applyPaddingToPlainText(plaintextBytes);
        byte[] expectedPaddedBytes = {1, 2, 3, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedPaddedBytes, paddedBytes);
    }

    @Test
    public void testPadding2() {
        // plaintext that is one bit less than a block
        byte[] plaintextBytes = {1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 50, 50, 50, 1, 22, 6, 7, 8};
        byte[] paddedBytes = HelperMethods.applyPaddingToPlainText(plaintextBytes);
        byte[] expectedPaddedBytes = {1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 50, 50, 50, 1, 22, 6, 7, 8, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedPaddedBytes, paddedBytes);
    }

    @Test
    public void testAESEncryptionAndDecryption() {
        // encrypt some bytes and decrypt them to get the original bytes
        try {
            Assignment1 program = new Assignment1();
            byte[] plaintextBytes = Files.readAllBytes(Paths.get("Assignment1.class"));
            byte[] paddedPlaintextBytes = HelperMethods.applyPaddingToPlainText(plaintextBytes);

            byte[] AESKey = {37, -116, 113, -42, -128, -113, 25, 10, 69, 123, 54, -43, -8, -48, -60, -65, 123, 39, -54, 103, 51, 49, -28, -108, 18, 70, -78, -103, 91, 114, -97, 17};
            String IVString = HelperMethods.readFile("IV.txt");
            byte[] IVBytes = HelperMethods.decodeHexString(IVString);

            byte[] aesCipherBytes = program.encryptAES(paddedPlaintextBytes, IVBytes, AESKey);
            byte[] decryptedPlaintextBytes = program.decryptAES(aesCipherBytes, IVBytes, AESKey);

            assertArrayEquals(plaintextBytes, decryptedPlaintextBytes);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRSAEncryptionAndDecryption() {
        Assignment1 program = new Assignment1();

        try {
            String password = "X)Lmv!nux6A9G5%";
            byte[] passwordBytes = password.getBytes("UTF-8");

            BigInteger e = new BigInteger("65537", 10);
            String publicModulus = "90869450415146058688797477200948425302945076773547174098735" +
                    "9289561440861968860814477403774349719761641670312566894138086649334908" +
                    "8794356554895149433555027";
            BigInteger N = new BigInteger(publicModulus, 10);

            // 1. Encryption password with RSA:
            byte[] rsaEncryption = program.encryptRSA(passwordBytes, e, N);

            // 2. Decrypt password with RSA:
            String privateExponent = "89365058183270423953039885874475912959479623544084" +
                    "4479456143566699940284657762576258282420226939967257905899144258740638" +
                    "4754958587400493169361356902030209";
            BigInteger d = new BigInteger(privateExponent, 10);
            byte[] rsaDecryption = program.encryptRSA(rsaEncryption, d, N);

            assertArrayEquals(passwordBytes, rsaDecryption);
        } catch(Exception e) {
           e.printStackTrace();
        }
    }
}
