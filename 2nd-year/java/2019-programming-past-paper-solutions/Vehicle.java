public class Vehicle {
    private int numWheels;
    private int numDoors;

    Vehicle() {
        numWheels = 4;
        numDoors = 4;
    }

    Vehicle(int wheels, int doors) {
        numWheels = wheels;
        numDoors = doors;
    }

    public int getNumWheels() {
        return numWheels;
    }

    public void setNumWheels(int wheels) {
        numWheels = wheels;
    }

    public int getNumDoors() {
        numDoors = doors;
    }

    public void setNumDoors(int doors) {
        numDoors = doors;
    }

    public String toString() {
        return "Number of wheels" + ": " + numWheels + ", " + "number of doors: " + numDoors;
    }
}
