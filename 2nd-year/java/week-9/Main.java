import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
   public static void main(String [] args)
   {
      Movie [] movies = {
         new Movie("James Bond does Java", Movie.NEW_RELEASE),
         new Movie("Mickey Mouse", Movie.CHILDRENS),
         new Movie("The Pointer Sisters", Movie.REGULAR),
         new Movie("The Dointer Sisters", Movie.NEW_RELEASE),
      };
      Rental [] rentalsArray = {
         new Rental(movies[0], 1),
         new Rental(movies[1], 10),
         new Rental(movies[2], 2),
         new Rental(movies[3], 2),
      };

      List<Rental> rentals = new ArrayList<Rental>(Arrays.asList(rentalsArray));

      int count = 0;

      for(Rental rental : rentals)
      {
         System.out.println("Rental " + ++count);
         System.out.println("    " + rental.getMovie());
         System.out.println("    " + rental.getDaysRented());
         System.out.println("    The frequent renter points are " + rental.getFrequentRenterPoints());
      }
   }
}