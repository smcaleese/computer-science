import java.util.ArrayList;

public class Car
{
    int numDoors;
    public Car(int numDoors) {
        this. numDoors = numDoors;
    }

    public ArrayList<Car> getCarsWithFourDoors(ArrayList<Car> cars) {
        ArrayList<Car> output = new ArrayList<Car>();

        for(int i = 0; i < cars.size(); i++) {
            if(cars.get(i).numDoors == 4) {
                output.add(cars.get(i));
            }
        }
        return output;
    }
}