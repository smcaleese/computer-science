import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        try {
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

            BigInteger secretKey = new BigInteger("1010101010101010", 16);
            BigInteger k = new BigInteger("1151008269", 10);

            BigInteger y = assignment2.generateY(generator, secretKey, primeModulus);
            System.out.println("y: " + y);

            BigInteger r = assignment2.generateR(generator, k, primeModulus);
            System.out.println("r: " + r);

            byte[] imageBytes = Files.readAllBytes(Paths.get("lenna.png"));

            BigInteger s = assignment2.generateS(imageBytes, secretKey, r, k, primeModulus);
            System.out.println("s: " + s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
