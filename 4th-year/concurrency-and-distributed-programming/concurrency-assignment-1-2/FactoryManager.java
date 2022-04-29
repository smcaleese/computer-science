import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Robot {
    public int stepName;
    public ReentrantLock lock;

    public Robot(int stepName) {
        this.stepName = stepName;
        this.lock = new ReentrantLock(true);
    }
}
    
// Class for a FactoryLine, which has an array of robots.
class FactoryLine {
    public Robot[] robots;
    public int lineNumber;
    
    public FactoryLine(int numRobots, int lineNumber) {
        robots = new Robot[numRobots];
        for (int i = 0; i < numRobots; i++) {
            robots[i] = new Robot(i);
        }
        this.lineNumber = lineNumber;
    }
    
    public Robot getRobot(int index) {
        return robots[index];
    }
}

/* Class for the FactoryManager, which receives orders from the user and assigns them to a factory line once a robot and a car from
warehouse is available. */
public class FactoryManager extends Thread {
    private FactoryLine[] lines;
    private int carArrivalProbability;
    private int tickDuration;
    private int numDealerships;
    private DeliveryTruck deliveryTruck;
    private int numOrders;
    private String[] models = {"A", "B", "C"};
    private String[] colors = {"red", "green", "blue"};
    public boolean lastOrderReceived = false;
    
    public FactoryManager(int carArrivalProbability, int numLines, int tickDuration, int numDealerships, int numOrders) {
        lines = new FactoryLine[numLines];
        for (int i = 0; i < numLines; i++) {
            lines[i] = new FactoryLine(5, i);
        }
        this.carArrivalProbability = carArrivalProbability;
        this.tickDuration = tickDuration;
        this.numDealerships = numDealerships;
        deliveryTruck = new DeliveryTruck(numDealerships, 3);
        this.numOrders = numOrders;
    }

    public void printFactoryLines() {
        ArrayList<String> lineStates = new ArrayList<>();
        for(FactoryLine line : lines) {
            StringBuilder lineState = new StringBuilder();
            for(int i = 0; i < line.robots.length; i++) {
                if(line.robots[i].lock.isLocked()) {
                    lineState.append("X");
                } else {
                    lineState.append("O");
                }
            }
            lineStates.add(lineState.toString());
        }
        System.out.printf("[%d] Factory lines: %s%n", System.currentTimeMillis(), String.join("-", lineStates));
    }

    public void run() {
        Warehouse warehouse = new Warehouse(models, tickDuration, this);
        warehouse.start();

        int orderNumber = 0;
        while (orderNumber < numOrders) {
            try {
                Thread.sleep(tickDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Random rand = new Random();
            if (rand.nextInt(100) < carArrivalProbability) {
                Car car = null;
                int lineIndex = rand.nextInt(lines.length);
                int dealership = rand.nextInt(numDealerships);
                int colorIndex = rand.nextInt(3);
                int modelIndex = rand.nextInt(3);

                if(!warehouse.hasModel(models[modelIndex])) {
                    System.out.printf("Warehouse not does have model. Order rejected. %n");
                    continue;
                }
                warehouse.warehouseLock.lock();
                warehouse.removeModel(models[modelIndex]);
                warehouse.warehouseNotFull.signal();
                warehouse.warehouseLock.unlock();

                car = Car.createCar(modelIndex, colors[colorIndex], lines[lineIndex], orderNumber, deliveryTruck, dealership, this);
                car.start();
                orderNumber++;
            }
        }
        lastOrderReceived = true;
        System.out.printf("[%d] (%d) Last order received. Dealerships stopping. %n%n", System.currentTimeMillis(), orderNumber);
    }
}

