import java.util.*;
import java.time.*;
import java.time.format.*;
import java.util.concurrent.locks.*;

/*
FactoryManager contains the methods for operating the factory.
It is also a monitor for coordinating the interaction of threads using locks and conditions.
 */
public class FactoryManager extends Thread implements Variables {
    int tickRate;
    int numModels;
    int numColors;
    int numProductionLines;
    int maxTicks;
    int numDealerships;
    int[] productionLineSettings;

    Warehouse warehouse;

    Dealership[] dealerships;
    CarCarrierTrailer[] carCarrierTrailers;
    Condition[] carCarrierConditions;

    Factory factory;
    Robot[][] productionLines;
    Condition[][] productionLineConditions;
    Car[][] productionLineStates;

    Lock lock = new ReentrantLock();
    Condition warehouseNotFull = lock.newCondition();
    Condition canAddToProductionLine = lock.newCondition();
    Condition canAddToOrderQueue = lock.newCondition();

    ArrayList<Order> orderQueue = new ArrayList<>();
    int orderQueueCapacity = 10;
    int orderId = 0;

    void createDealerships() {
        dealerships = new Dealership[numDealerships];
        carCarrierTrailers = new CarCarrierTrailer[numDealerships];
        carCarrierConditions = new Condition[numDealerships];

        for(int dealershipId = 0; dealershipId < numDealerships; dealershipId++) {
            Map<String, Integer> dealershipSettings = Map.of(
                "tickRate", tickRate,
                "maxTicks", maxTicks,
                "numModels", numModels,
                "numColors", numColors,
                "dealershipId", dealershipId
            );
            dealerships[dealershipId] = new Dealership(this, dealershipSettings);
            carCarrierTrailers[dealershipId] = new CarCarrierTrailer(this, dealershipId, tickRate);
            carCarrierConditions[dealershipId] = lock.newCondition();
        }
    }

    void startDealerships() {
        for(Dealership dealership : dealerships) {
            dealership.start();
        }
        for(CarCarrierTrailer trailer : carCarrierTrailers) {
           trailer.start();
        }
    }

    void createProductionLines(int numProductionLines, int numRobots, int[] productionLineSettings) {
        productionLines = new Robot[numProductionLines][numRobots];
        productionLineConditions = new Condition[numProductionLines][numRobots];
        productionLineStates = new Car[numProductionLines][numRobots];

        for(int i = 0; i < numProductionLines; i++) {
            for(int j = 0; j < numRobots; j++) {
                productionLines[i][j] = new Robot(this, i, j, productionLineSettings[j]);
                productionLineConditions[i][j] = lock.newCondition();
                productionLineStates[i][j] = null;
            }
        }
    }

    public void startProductionLines() {
        for(int i = 0; i < productionLines.length; i++) {
            for(int j = 0; j < productionLines[i].length; j++) {
                productionLines[i][j].start();
            }
        }
    }

    public FactoryManager(Map<String, Integer> settings, int[] productionLineSettings) {
        System.out.println("Factory manager initialized");

        tickRate = settings.get("tickRate");
        numModels = settings.get("numModels");
        numColors = settings.get("numColors");
        numProductionLines = settings.get("numProductionLines");
        maxTicks = settings.get("maxTicks");
        numDealerships = settings.get("numDealerships");
        this.productionLineSettings = productionLineSettings;

        warehouse = new Warehouse(this, tickRate, numModels, orderQueue);

        createDealerships();

        createProductionLines(numProductionLines, productionLineSettings.length, productionLineSettings);
        factory = new Factory(this);
    }

    public void run() {
        startDealerships();
        warehouse.start();
        factory.start();
    }

    public String getCurrentTime() {
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
    }

    public void addCarsToWarehouse() throws InterruptedException {
        lock.lock();
        try {
            while(warehouse.getWarehouseSize() == warehouse.getWarehouseCapacity()) {
                warehouseNotFull.await();
            }
            String randomModel = Variables.models[new Random().nextInt(numModels)];
            String nextModel = orderQueue.size() > 0 ? orderQueue.get(0).getModel() : randomModel;
            Car newCar = new CarFactory().getCar(-1, -1, "unpainted", nextModel);
            warehouse.addToWarehouse(nextModel, newCar);
            System.out.printf("(%s) 1 vehicle of model %s added to the warehouse at time %s => %d vehicles of model %s in total %n",
                    Thread.currentThread().getId(), newCar.getModel(), getCurrentTime(), warehouse.getWarehouseSize(nextModel), newCar.getModel()
            );
            canAddToProductionLine.signal();
        } finally {
            lock.unlock();
        }
    }

    public void addOrderToQueue(int dealershipId, String model, String color, int price) throws InterruptedException {
        lock.lock();
        try {
            if(orderQueue.size() == orderQueueCapacity) {
                canAddToOrderQueue.await();
            }
            Order newOrder = new Order(dealershipId, orderId, model, color, price);
            orderQueue.add(newOrder);
            // sort by price in descending order
            Collections.sort(orderQueue, (a, b) -> Integer.compare(b.price, a.price));
            System.out.printf("(%s) Received order %s for vehicle model %s, colour %s at time %s => %d orders remaining. %n",
                    Thread.currentThread().getId(), orderId, newOrder.getModel(), newOrder.getColor(), getCurrentTime(), orderQueue.size()
            );
            orderId++;
            canAddToProductionLine.signal();
        } finally {
            lock.unlock();
        }
    }

    public void printProductionLines() {
        ArrayList<String> states = new ArrayList<>();
        for(Car[] productionLineState : productionLineStates) {
            String s = "";
            for(Car robotState : productionLineState) {
                if (robotState != null) {
                    s += Integer.toString(robotState.orderId);
                } else {
                    s += "X";
                }
            }
            states.add(s);
        }
        String output = String.join("-", states);
        System.out.println("Production lines state: " + output);
    }

    int getFirstFreeProductionLine() {
        for(int i = 0; i < productionLineStates.length; i++) {
            if(productionLineStates[i][0] == null) {
                return i;
            }
        }
        return -1;
    }

    public void addToProductionLine() throws InterruptedException {
        lock.lock();
        try {
            while(orderQueue.size() == 0 || warehouse.getWarehouseSize(orderQueue.get(0).getModel()) == 0 || getFirstFreeProductionLine() == -1) {
                canAddToProductionLine.await();
            }
            Order nextOrder = orderQueue.remove(0);
            canAddToOrderQueue.signalAll();
            Car unpaintedCar = warehouse.removeFirst(nextOrder.getModel());
            warehouseNotFull.signal();
            Car car = new CarFactory().getCar(nextOrder.getDealershipId(), nextOrder.getOrderId(), nextOrder.getColor(), unpaintedCar.getModel());
            int freeProductionLine = getFirstFreeProductionLine();
            System.out.printf("(%s) Order %s moving to production line %d at time %s => %d orders and %d vehicles of model %s remaining %n",
                Thread.currentThread().getId(), car.getOrderId(), freeProductionLine, getCurrentTime(),
                orderQueue.size(), warehouse.getWarehouseSize(nextOrder.getModel()), nextOrder.getModel()
            );
            productionLineStates[freeProductionLine][0] = car;
            productionLineConditions[freeProductionLine][0].signal();
        } finally {
            lock.unlock();
        }
    }

    public void robotWork(int lineIndex, int position, int delay) throws InterruptedException {
        lock.lock();
        try {
            // 0. wait until an order to passed to the robot
            while(productionLineStates[lineIndex][position] == null) {
               productionLineConditions[lineIndex][position].await();
            }
            // wait for next robot to finish
            while(position + 1 < productionLineSettings.length && productionLineStates[lineIndex][position + 1] != null) {
                productionLineConditions[lineIndex][position].await();
            }
            // 1. working for delay
            Thread.sleep(delay * tickRate);
            printProductionLines();
            canAddToProductionLine.signal();
            Car currentCar = productionLineStates[lineIndex][position];

            if(position + 1 < productionLineSettings.length) {
                // 2. move the car to the next robot for 0.1s (two states busy)
                productionLineStates[lineIndex][position + 1] = currentCar;
                Thread.sleep(100 * tickRate);
                // 3. remove the car from the previous robot
                productionLineStates[lineIndex][position] = null;
                // 4. tell the next robot to start
                productionLineConditions[lineIndex][position + 1].signal();
            } else {
                // tell the car carrier trailer associated with the dealershipId to pick up the order
                carCarrierConditions[currentCar.getDealershipId()].signal();
                productionLineConditions[lineIndex][position].await();
            }
        } finally {
            lock.unlock();
        }
    }

    int findPaintedCar(int dealershipId) {
        for(int lineIndex = 0; lineIndex < productionLineStates.length; lineIndex++) {
            Car[] lineState = productionLineStates[lineIndex];
            int endIndex = lineState.length - 1;
            if(lineState[endIndex] != null && lineState[endIndex].getDealershipId() == dealershipId) {
                return lineIndex;
            }
        }
        return -1;
    }

    public void wakeProductionLine(int lineIndex) {
        for(Condition condition : productionLineConditions[lineIndex]) {
            condition.signal();
        }
    }

    public void addToTrailer(int dealershipId) throws InterruptedException {
        lock.lock();
        try {
            while(findPaintedCar(dealershipId) == -1) {
                carCarrierConditions[dealershipId].await();
            }
            int lineIndex = findPaintedCar(dealershipId);
            // remove the car from the end of the production line
            Car finishedCar = productionLineStates[lineIndex][productionLineSettings.length - 1];
            productionLineStates[lineIndex][productionLineSettings.length - 1] = null;
            // tell the other robots to continue
            wakeProductionLine(lineIndex);
            // add the car to the trailer
            carCarrierTrailers[dealershipId].addToTrailer(finishedCar);
            System.out.printf("(%s) Order %s moving to car carrier trailer %d at time %s %n",
                    Thread.currentThread().getId(), finishedCar.getOrderId(), finishedCar.getDealershipId(), getCurrentTime()
            );
            if(carCarrierTrailers[dealershipId].trailerIsFull()) {
                System.out.printf("(%s) Car carrier trailer %d departed at time %s %n%n",
                        Thread.currentThread().getId(), finishedCar.getDealershipId(), getCurrentTime()
                );
                carCarrierTrailers[dealershipId].emptyTrailer();
            }
        } finally {
            lock.unlock();
        }
    }
}
