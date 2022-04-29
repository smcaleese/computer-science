public class Rental {
    private Movie _movie;
    private int _daysRented;

    public Rental(Movie movie, int daysRented) {
        _movie = movie;
        _daysRented = daysRented;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    public String toString() {
        String retStr = this.getMovie() + " [" + this.getDaysRented() + "]";
        return retStr;
    }

    public double getCharge() {
        float amount = 0;
        switch(getMovie().getPriceCode()) {
            case Movie.REGULAR:
                amount += 2;
                if (getDaysRented() > 2)
                        amount += (getDaysRented() - 2) * 1.5;
            break;
            case Movie.NEW_RELEASE:
                amount += getDaysRented() * 3;
            break;
            case Movie.CHILDRENS:
                amount += 1.5;
                if (getDaysRented() > 3)
                    amount += (getDaysRented() - 3) * 1.5;
            break;
        }
        return amount;
    }

    public int getFrequentRenterPoints() {
        int frequentRenterPoints = 1;
        if(getMovie().getPriceCode() == Movie.NEW_RELEASE && _daysRented > 1)
            frequentRenterPoints++;
        return frequentRenterPoints;
    }
}
