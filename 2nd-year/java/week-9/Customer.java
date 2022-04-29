import java.util.*;

public class Customer {
    private String _name;
    private List<Rental> _rentals;

    public Customer(String name, List<Rental> rentals) {
        _name = name;
        _rentals = rentals;
    }

    public String getName() {
        return _name;
    }

    public List<Rental> getRentals() {
        return _rentals;
    }

    public void addRental(Rental rental) {
        _rentals.add(rental);
    }

    public String toString() {
        return this.getName() + ": " + this.getRentals();
    }

    public String statement()
    {
        String output = "<h1>Statement for " + getName() + "</h1>\n";

        output += "<ol>";
        for(Rental rental : getRentals())
            output += "  <li>" + rental.getMovie().getTitle() + "  " + rental.getCharge() + "</li>\n";
        output += "</ol>";

        output += "<p>Amount owed is " + getTotalCharge() + "</p>\n";
        output += "<p>You earned " + getTotalFrequentRenterPoints()  + " frequent renter points</p>\n";
        return output;
    }

    public double getTotalCharge() {
        double totalCharge = 0;
        for(Rental rental : getRentals()) {
            totalCharge += rental.getCharge();
        }
        return totalCharge;
    }

    public int getTotalFrequentRenterPoints() {
        int totalFrequentRenterPoints = 0;
        for(Rental rental : getRentals()) {
            totalFrequentRenterPoints += rental.getFrequentRenterPoints();
        }
        return totalFrequentRenterPoints;
    }
}
