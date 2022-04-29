import java.util.*;
import java.util.concurrent.locks.*;

public class Warehouse extends Thread {
    ArrayList<String> warehouse;
    int tickDuration;
    String[] models;
    FactoryManager factoryManager;
    public ReentrantLock warehouseLock = new ReentrantLock();
    public Condition warehouseNotFull = warehouseLock.newCondition();
    int capacity = 5;

    public Warehouse(String[] models, int tickDuration, FactoryManager factoryManager) {
        this.models = models;
        this.tickDuration = tickDuration;
        this.factoryManager = factoryManager;
        warehouse = new ArrayList<>();
    }

    public boolean hasModel(String model) {
        return warehouse.contains(model);
    }

    public void addRandomModel() throws InterruptedException {
        warehouseLock.lock();
        int randomModelIndex = new Random().nextInt(models.length);
        String randomModel = models[randomModelIndex];
        warehouse.add(randomModel);
        if(warehouse.size() == capacity) {
            System.out.printf("[%d] Warehouse full.%n", System.currentTimeMillis());
            warehouseNotFull.await();
        }
        System.out.printf("[%d] Added model %s to warehouse.%n", System.currentTimeMillis(), randomModel);
        warehouseLock.unlock();
    }

    public void removeModel(String model) {
        warehouseLock.lock();
        warehouse.remove(model);
        warehouseLock.unlock();
    }

    public void run() {
        System.out.printf("[%d] Warehouse started.%n", System.currentTimeMillis());

        while(!factoryManager.lastOrderReceived) {
            try {
                addRandomModel();

                Thread.sleep(tickDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
