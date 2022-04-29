import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Class for the DeliveryTruck, which receives cars and assigns them to the queue of n different trucks.
// Each truck has a ReentrantLock and a condition which is signalled when the truck is ready to receive a car.
class DeliveryTruck {
    private int numTrucks;
    private ReentrantLock[] truckLocks;
    public Condition[] truckConditions;
    private ArrayList<Car>[] truckQueues;
    private int[] truckCapacities;

    public DeliveryTruck(int numTrucks, int truckCapacity) {
        this.numTrucks = numTrucks;
        truckLocks = new ReentrantLock[numTrucks];
        truckConditions = new Condition[numTrucks];
        truckQueues = new ArrayList[numTrucks];
        truckCapacities = new int[numTrucks];
        for (int i = 0; i < numTrucks; i++) {
            truckLocks[i] = new ReentrantLock();
            truckConditions[i] = truckLocks[i].newCondition();
            truckQueues[i] = new ArrayList<Car>();
            truckCapacities[i] = truckCapacity;
        }
    }

    public void addCar(Car car) {
        int truckIndex = car.dealership;
        truckLocks[truckIndex].lock();
        if (truckQueues[truckIndex].size() == truckCapacities[truckIndex]) {
            try {
                System.out.printf("[%d] (%d) Waiting for truck %d to be ready%n", System.currentTimeMillis(), car.carNumber, truckIndex);
                truckConditions[truckIndex].await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        truckQueues[truckIndex].add(car);
        System.out.printf("[%d] (%d) Added car %d to truck %d, capacity %d/%d%n",
                System.currentTimeMillis(), car.carNumber, car.carNumber, truckIndex, truckQueues[truckIndex].size(), truckCapacities[truckIndex]
        );

        if (truckQueues[truckIndex].size() == truckCapacities[truckIndex]) {
            System.out.printf("[%d] (%d) Truck %d is full%n", System.currentTimeMillis(), car.carNumber, truckIndex);
            deliverCars(truckIndex);
        }

        truckLocks[truckIndex].unlock();
    }

    void deliverCars(int i) {
        while (truckQueues[i].size() > 0) {
            System.out.printf("[%d] (%d) Delivering car %d to dealership %d%n",
                    System.currentTimeMillis(), truckQueues[i].get(0).carNumber, truckQueues[i].get(0).carNumber, i
            );
            truckQueues[i].remove(0);
        }
        truckConditions[i].signalAll();
    }
}
