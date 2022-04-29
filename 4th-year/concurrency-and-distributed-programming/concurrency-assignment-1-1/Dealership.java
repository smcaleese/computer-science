/*
An order for a vehicle A arrives with probability of 10% every 10 tics (orders stop arriving at
2500 tics)

The dealership should create orders and add them to a queue in the factory.

Each dealership could have its own queue and submits orders to the factory when the factory is ready
to receive new orders.

 */

import java.util.*;

class Order {
    int orderId;
    String model;
    String color;
    int price;
    int dealershipId;

    public Order(int dealershipId, int orderId, String model, String color, int price) {
        this.dealershipId = dealershipId;
        this.orderId = orderId;
        this.model = model;
        this.color = color;
        this.price = price;
    }

    public int getDealershipId() { return dealershipId; }
    public int getOrderId() { return orderId; }
    public String getColor() { return color; }
    public String getModel() { return model; }
}

class CarCarrierTrailer extends Thread {
    FactoryManager factoryManager;
    int dealershipId;
    int tickRate;
    int capacity = 5;
    ArrayList<Car> trailer = new ArrayList<>();

    public CarCarrierTrailer(FactoryManager factoryManager, int dealershipId, int tickRate) {
        this.factoryManager = factoryManager;
        this.dealershipId = dealershipId;
        this.tickRate = tickRate;
    }

    public void addToTrailer(Car car) {
        trailer.add(car);
    }

    public boolean trailerIsFull() {
        return trailer.size() == capacity;
    }

    public void emptyTrailer() {
        trailer.clear();
    }

    public void run() {
        System.out.printf("Car carrier trailer for dealership %s started %n", dealershipId);
        while(true) {
            try {
                factoryManager.addToTrailer(dealershipId);
                Thread.sleep(10 * tickRate);
            } catch(InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

public class Dealership extends Thread implements Variables {
    FactoryManager factoryManager;
    int dealershipId;
    int tickRate;
    int maxTicks;
    int numModels;
    int numColors;
    int currentTicks = 0;

    public Dealership(FactoryManager factoryManager, Map<String, Integer> settings) {
        this.factoryManager = factoryManager;
        this.tickRate = settings.get("tickRate");
        this.maxTicks = settings.get("maxTicks");
        this.numModels = settings.get("numModels");
        this.numColors = settings.get("numColors");
        this.dealershipId = settings.get("dealershipId");
    }

    public void run() {
        System.out.println("Dealership started");
        try {
            while(currentTicks < maxTicks) {
                // 10% probability of adding an order every 10 ticks
                if(new Random().nextInt(100) < 10) {
                    Random rand = new Random();
                    String randomColor = Variables.colors[rand.nextInt(numColors)];
                    String randomModel = Variables.models[rand.nextInt(numModels)];
                    int price = Variables.prices.get(randomModel);
                    factoryManager.addOrderToQueue(dealershipId, randomModel, randomColor, price);
                }
                currentTicks += 10;
                Thread.sleep(10 * tickRate);
            }
            if(currentTicks == maxTicks) {
                System.out.printf("Final order in dealership %d received %n%n", dealershipId);
            }
        } catch(InterruptedException e) {
            System.out.println(e);
        }
    }
}

