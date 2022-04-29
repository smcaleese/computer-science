import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Assignment2 implements Assignment2Interface {
    public BigInteger calculateSecretKey(BigInteger primeModulus) {
        SecureRandom rand = new SecureRandom();
        int randomInt = 0;
        while(randomInt < 2) {
            randomInt = rand.nextInt(primeModulus.intValue() - 1);
        }
        return BigInteger.valueOf(randomInt);
    }

    public static BigInteger modExp(BigInteger a, BigInteger x, BigInteger n) {
        // modular exponentiation using the left-to-right method
        String binExponent = x.toString(2);
        int k = binExponent.length();
        BigInteger y = BigInteger.valueOf(1);
        for(int i = 0; i < k; i++) {
            y = y.multiply(y).mod(n);
            if(binExponent.charAt(i) == '1') {
                y = y.multiply(a).mod(n);
            }
        }
        return y;
    }

    public BigInteger generateY(BigInteger generator, BigInteger secretKey, BigInteger primeModulus) {
        return Assignment2.modExp(generator, secretKey, primeModulus);
    }

    public BigInteger calculateGCD(BigInteger val1, BigInteger val2) {
        BigInteger high = val1.compareTo(val2) >= 0 ? val1 : val2;
        BigInteger low = val1.compareTo(val2) == -1 ? val1 : val2;
        while(low.compareTo(BigInteger.valueOf(0)) != 0) {
            BigInteger remainder = high.mod(low);
            high = low;
            low = remainder;
        }
        return high;
    }

    public BigInteger generateK(BigInteger primeModulus) {
        SecureRandom rand = new SecureRandom();
        BigInteger pMinusOne = primeModulus.subtract(BigInteger.valueOf(1));
        int k = rand.nextInt(pMinusOne.intValue());
        BigInteger gcd = calculateGCD(pMinusOne, BigInteger.valueOf(k));
        while(gcd.intValue() != 1) {
            k = rand.nextInt(pMinusOne.intValue());
            gcd = calculateGCD(pMinusOne, BigInteger.valueOf(k));
        }
        return BigInteger.valueOf(k);
    }

    public BigInteger generateR(BigInteger generator, BigInteger k, BigInteger modulus) {
        return Assignment2.modExp(generator, k, modulus);
    }

    private BigInteger[] calculateInverseRecursively(BigInteger a, BigInteger b) {
        // source: https://www.geeksforgeeks.org/python-program-for-basic-and-extended-euclidean-algorithms-2/
        if(a.compareTo(BigInteger.valueOf(0)) == 0) {
            return new BigInteger[] {b, BigInteger.valueOf(0), BigInteger.valueOf(1)};
        }
        BigInteger[] result = calculateInverseRecursively(b.mod(a), a);
        BigInteger gcd = result[0];
        BigInteger x1 = result[1];
        BigInteger y1 = result[2];
        BigInteger x = y1.subtract(b.divide(a).multiply(x1));
        BigInteger y = x1;
        return new BigInteger[] {gcd, x, y};
    }

    public BigInteger calculateInverse(BigInteger val, BigInteger modulus) {
        BigInteger[] answers = calculateInverseRecursively(val, modulus);
        return answers[1];
    }

    public BigInteger generateS(byte[] messageHash, BigInteger secretKey, BigInteger r, BigInteger k, BigInteger modulus) {
        // Compute s = (H(m)-xr)k-1 (mod p-1)
        BigInteger hashNumber = new BigInteger(1, messageHash);
        BigInteger pMinusOne = modulus.subtract(BigInteger.valueOf(1));
        BigInteger kInverse = calculateInverse(k, pMinusOne);
        BigInteger s = hashNumber.subtract(secretKey.multiply(r)).multiply(kInverse).mod(pMinusOne);
        try {
            while(s.compareTo(BigInteger.valueOf(0)) == 0) {
                System.out.println("attempt: " + s);
                s = hashNumber.subtract(secretKey.multiply(r)).multiply(kInverse).mod(pMinusOne);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public void outputToFile(BigInteger bigInt, String fileName) throws IOException {
        String hex = bigInt.toString(16);
        FileWriter fw = new FileWriter(fileName);
        fw.write(hex);
        fw.close();
    }

    public boolean verifyHash(byte[] messageHash, BigInteger g, BigInteger p, BigInteger y, BigInteger r, BigInteger s) {
        // verify that g^(H(m)) (mod p) = y^(r)r^(s) (mod p)
        BigInteger hashNumber = new BigInteger(1, messageHash);
        BigInteger LHS = modExp(g, hashNumber, p);
        BigInteger RHS = modExp(y, r, p).multiply(modExp(r, s, p)).mod(p);
        return LHS.intValue() == RHS.intValue();
    }

    public static void main(String[] args) {
        Assignment2 assignment2 = new Assignment2();
        String p = "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6edd"
                + "ef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc"
                + "8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f"
                + "47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323";
        String g = "44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2"
                + "e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e8864"
                + "1a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f5496"
                + "64bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68";
        BigInteger primeModulus = new BigInteger(p, 16);
        BigInteger generator = new BigInteger(g, 16);
        try {
            // 1. generate y
            BigInteger secretKey = assignment2.calculateSecretKey(primeModulus);
            BigInteger y = assignment2.generateY(generator, secretKey, primeModulus);
            assignment2.outputToFile(y, "y.txt");

            // 2. sign message m
            BigInteger k = assignment2.generateK(primeModulus);
            System.out.println("k: " + k);

            BigInteger r = assignment2.generateR(generator, k, primeModulus);
            assignment2.outputToFile(r, "r.txt");

            byte[] classFileBytes = Files.readAllBytes(Paths.get("Assignment2.class"));
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = md.digest(classFileBytes);

            BigInteger s = assignment2.generateS(messageHash, secretKey, r, k, primeModulus);
            assignment2.outputToFile(s, "s.txt");

            // 3. create the signature
            String digitalSignatureHex = r.toString(16) + s.toString(16);

            // 4. verify that the signature is correct
            // 4.1 verificationPassed should be true
            boolean verificationPassed = assignment2.verifyHash(messageHash, generator, primeModulus, y, r, s);
            System.out.println("verificationPassed: " + verificationPassed);

        } catch(NoSuchAlgorithmException | IOException e) {
            System.out.print("Error: ");
            e.printStackTrace();
        }
    }
}
