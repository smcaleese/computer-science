import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;

class Assignment2Test {
    Assignment2 assignment2 = new Assignment2();
    @Test
    void testBigInteger() {
        // test hex to int and back
        String s = "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6edd"
                + "ef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc"
                + "8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f"
                + "47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323";
        BigInteger num = new BigInteger(s, 16);
        String hex2 = num.toString(16);
        assertEquals(s, hex2);
    }

    @Test
    void calculateSecretKey() {
        // check that 1 < x < p-1
        BigInteger p = new BigInteger("a", 16);
        for(int i = 0; i < 100; i++) {
            BigInteger x = assignment2.calculateSecretKey(p);
            assertTrue(x.intValue() > 1 && x.intValue() < p.intValue() - 1);
        }
    }

    @Test
    void modExp() {
        BigInteger a = BigInteger.valueOf(5);
        BigInteger x = BigInteger.valueOf(4);
        BigInteger n = BigInteger.valueOf(3);
        BigInteger result = Assignment2.modExp(a, x, n);
        assertEquals(BigInteger.valueOf(1), result);
    }

    @Test
    void generateY() {
        // check that y = g^x (mod p)
        BigInteger generator = BigInteger.valueOf(10);
        BigInteger secretKey = BigInteger.valueOf(11);
        BigInteger primeModulus = new BigInteger("ff", 16);
        BigInteger y = new Assignment2().generateY(generator, secretKey, primeModulus);
        assertEquals(BigInteger.valueOf(190), y);
    }

    @Test
    void calculateGCD() {
        BigInteger a = BigInteger.valueOf(43);
        BigInteger b = BigInteger.valueOf(62);
        BigInteger gcd = new Assignment2().calculateGCD(a, b);
        assertEquals(BigInteger.valueOf(1), gcd);
    }

    @Test
    void calculateK() {
        //  0 < k < p-1 and gcd(k,p-1) = 1
        int p = 50;
        BigInteger primeModulus = BigInteger.valueOf(p);
        BigInteger k = assignment2.generateK(primeModulus);
        for(int i = 0; i < 100; i++) {
            assertTrue(0 < k.intValue() && k.intValue() < p - 1);
            BigInteger gcd = assignment2.calculateGCD(k, primeModulus.subtract(BigInteger.valueOf(1)));
            assertEquals(BigInteger.valueOf(1), gcd);
        }
    }

    @Test
    void generateR() {
        int p = 50;
        BigInteger primeModulus = new BigInteger("f", 16);
        BigInteger k = assignment2.generateK(primeModulus);

        BigInteger generator = BigInteger.valueOf(5);
        // r = g^k (mod p)
        BigInteger r = assignment2.generateR(generator, k, primeModulus);
        double expectedValue = Math.pow(generator.doubleValue(), k.doubleValue()) % primeModulus.doubleValue();
        assertEquals(Math.round(expectedValue), r.intValue());
    }

    @Test
    void calculateInverse1() {
        BigInteger val = BigInteger.valueOf(67);
        BigInteger modulus = BigInteger.valueOf(119);
        BigInteger inverse = assignment2.calculateInverse(val, modulus);
       assertEquals(16, inverse.intValue());
    }

    @Test
    void calculateInverse2() {
        BigInteger val = BigInteger.valueOf(3);
        BigInteger modulus = BigInteger.valueOf(46);
        BigInteger inverse = assignment2.calculateInverse(val, modulus);
        assertEquals(-15, inverse.intValue());
    }

    @Test
    void generateS() {
        byte[] plaintext = {6};
        BigInteger secretKey = BigInteger.valueOf(5);
        BigInteger r = BigInteger.valueOf(13);
        BigInteger k = BigInteger.valueOf(3);
        BigInteger modulus = BigInteger.valueOf(47);
        BigInteger s = assignment2.generateS(plaintext, secretKey, r, k, modulus);
        assertEquals(11, s.intValue());
    }

    @Test
    void verifyHash() {
        // verify that g^(H(m)) (mod p) = y^(r)r^(s) (mod p)
        BigInteger g = BigInteger.valueOf(10);
        byte[] hash = {6};
        BigInteger p = BigInteger.valueOf(47);
        BigInteger y = BigInteger.valueOf(31);
        BigInteger r = BigInteger.valueOf(13);
        BigInteger s = BigInteger.valueOf(11);
        boolean result = assignment2.verifyHash(hash, g, p, y, r, s);
        assertTrue(result);
    }

    @Test
    void main() {
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

            // 2. sign message m
            BigInteger k = assignment2.generateK(primeModulus);
            BigInteger r = assignment2.generateR(generator, k, primeModulus);

            byte[] classFileBytes = Files.readAllBytes(Paths.get("/home/stephen/assignments/cryptography-and-security-protocols/assignment-2/src/main/java/Assignment2.class"));
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = md.digest(classFileBytes);

            BigInteger s = assignment2.generateS(messageHash, secretKey, r, k, primeModulus);

            // 3. create the signature
            String digitalSignatureHex = r.toString(16) + s.toString(16);

            // 4. verify the signature
            assertTrue(assignment2.verifyHash(messageHash, generator, primeModulus, y, r, s));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}