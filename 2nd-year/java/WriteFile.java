import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class ReadFile
{
    public static void main(String [] args) throws FileNotFoundException
    {
        PrintWriter out = new PrintWriter("test.out.txt");
        out.println(6.8);
        out.println("Hello");
        out.println(10);
        out.close();
    }
}