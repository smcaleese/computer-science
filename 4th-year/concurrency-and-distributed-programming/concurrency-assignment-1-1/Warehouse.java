import java.util.*;

public class Warehouse extends Thread implements Variables {
    FactoryManager factoryManager;
    int tickRate;
    HashMap<String, LinkedList<Car>> warehouses;
    int warehouseCapacity;
    ArrayList<Order> orderQueue;

    public void createWarehouses(int numModels) {
        for(int i = 0; i < numModels; i++) {
            warehouses.put(Variables.models[i], new LinkedList<>());
        }
    }

    public Warehouse(FactoryManager factoryManager, int tickRate, int numModels, ArrayList<Order> orderQueue) {
        this.factoryManager = factoryManager;
        this.tickRate = tickRate;
        warehouses = new HashMap<>();
        warehouseCapacity = 10;
        createWarehouses(numModels);
        this.orderQueue = orderQueue;
    }

    public int getWarehouseCapacity() { return warehouseCapacity; }

    public int getWarehouseSize() {
        int count = 0;
        for(String model : warehouses.keySet()) {
            count += warehouses.get(model).size();
        }
        return count;
    }

    public int getWarehouseSize(String modelType) {
        return warehouses.get(modelType).size();
    }

    public void addToWarehouse(String modelType, Car unpaintedCar) {
        warehouses.get(modelType).add(unpaintedCar);
    }

    public Car removeFirst(String model) {
        return warehouses.get(model).removeFirst();
    }

    public void run() {
        System.out.println("Warehouse started");
        while(true) {
            try {
                factoryManager.addCarsToWarehouse();
                Thread.sleep(100 * tickRate);
            } catch(InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
