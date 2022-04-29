import java.util.*;
import java.security.*;
import java.math.*;
import java.io.*;
import java.lang.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class HelperMethods {
    public static String readFile(String fileName) {
        String fileText = "";
        try {
            Scanner fileReader = new Scanner(new File(fileName));
            fileText = fileReader.nextLine();
            fileReader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return fileText;
    }

    // source: https://www.titanwolf.org/Network/q/9557c454-788c-4bbe-85d6-1cef34ed4985/y
    public static byte[] binaryStringToByteArray(String binaryString) {
        int splitSize = 8;

        if(binaryString.length() % splitSize == 0){
            int index = 0;
            int position = 0;

            byte[] resultByteArray = new byte[binaryString.length()/splitSize];
            StringBuilder text = new StringBuilder(binaryString);

            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArray[position] = byteAsInt.byteValue();
                index += splitSize;
                position ++;
            }
            return resultByteArray;
        }
        else{
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" +binaryString+"' % 8 != 0");
            return null;
        }
    }

    public static byte[] mergeArrays(byte[] a, byte[] b) {
        byte[] mergedArray = new byte[a.length + b.length];
        System.arraycopy(a, 0, mergedArray, 0, a.length);
        System.arraycopy(b, 0, mergedArray, a.length, b.length);
        return mergedArray;
    }

    public static String byteArrayToBitString(byte[] byteArray) {
        String bitString = "";
        for(byte b : byteArray) {
            String s = b < 0 ? Integer.toBinaryString(256 + b) : Integer.toBinaryString(b);
            // pad start of bit string with zeros so that there are always eight bits
            int paddingLength = 8 - s.length();
            for(int i = 0; i < paddingLength; i++) {
                s = "0" + s;
            }
            bitString += s;
        }
        return bitString;
    }

    public static byte[] applyPaddingToPlainText(byte[] plaintextBytes) {
        String plaintextBits = HelperMethods.byteArrayToBitString(plaintextBytes);

        if(plaintextBits.length() % 128 == 0) {
            // add new block
            plaintextBits += "1";
            for(int i = 0; i < 127; i++) {
                plaintextBits += "0";
            }
        } else {
            // pad last block
            int totalBlockBits = (Math.floorDiv(plaintextBits.length(), 128) + 1) * 128;
            int paddingLength = totalBlockBits - plaintextBits.length();
            plaintextBits += "1";
            for(int i = 0; i < paddingLength - 1; i++) {
                plaintextBits += "0";
            }
        }

        byte[] paddedPlaintextBytes = HelperMethods.binaryStringToByteArray(plaintextBits);
        return paddedPlaintextBytes;
    }

    public static byte[] removePadding(byte[] paddedPlaintextBytes) {
        String paddedPlaintextBitString = HelperMethods.byteArrayToBitString(paddedPlaintextBytes);
        int i = paddedPlaintextBitString.length() - 1;
        while(paddedPlaintextBitString.charAt(i) == '0') {
            i--;
        }
        // then get the substring up to zero
        String bitStringWithoutPadding = paddedPlaintextBitString.substring(0, i);
        byte[] plaintextBytes = HelperMethods.binaryStringToByteArray(bitStringWithoutPadding);
        return plaintextBytes;
    }

    // source: https://www.baeldung.com/java-byte-arrays-hex-strings
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    // source: https://www.baeldung.com/java-byte-arrays-hex-strings
    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    // source: https://www.baeldung.com/java-byte-arrays-hex-strings
    public static byte hexToByte(String hexString) {
        int firstDigit = HelperMethods.toDigit(hexString.charAt(0));
        int secondDigit = HelperMethods.toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    // source: https://www.baeldung.com/java-byte-arrays-hex-strings
    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(HelperMethods.byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    // source: https://www.baeldung.com/java-byte-arrays-hex-strings
    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = HelperMethods.hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    public static void outputStringToFile(String s, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(s);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
