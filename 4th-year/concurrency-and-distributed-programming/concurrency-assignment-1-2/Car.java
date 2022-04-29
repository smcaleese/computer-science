public class Car extends Thread {
    public int[] delays;
    private FactoryLine line;
    private String color;
    public int carNumber;
    private DeliveryTruck deliveryTruck;
    public String model;
    public int dealership;
    public FactoryManager factoryManager;

    public Car(String color, FactoryLine line, int carNumber, DeliveryTruck deliveryTruck, String model, int dealership, FactoryManager factoryManager) {
        this.color = color;
        this.line = line;
        this.carNumber = carNumber;
        this.deliveryTruck = deliveryTruck;
        this.model = model;
        this.dealership = dealership;
        this.factoryManager = factoryManager;
    }

    public void run() {
        System.out.printf("[%d] (%d) Order %d is a %s with color %s from dealership %d, assigned to production line %d%n",
                System.currentTimeMillis(), carNumber, carNumber, model, color, dealership, line.lineNumber
        );
        Robot previousRobot = null;
        for (int i = 0; i < delays.length; i++) {
            try {
                Robot robot = line.getRobot(i);
                if(robot.lock.isLocked()) {
                    System.out.printf("[%d] (%d) Waiting for robot %d, line %d%n", System.currentTimeMillis(), carNumber, robot.stepName, line.lineNumber);
                }
                robot.lock.lock();
                System.out.printf("[%d] (%d) Moving to robot %d, line %d%n", System.currentTimeMillis(), carNumber, robot.stepName, line.lineNumber);
                Thread.sleep(100);
                if(previousRobot != null) {
                    previousRobot.lock.unlock();
                }
                factoryManager.printFactoryLines();
                System.out.printf("[%d] (%d) Robot %d, line %d started working on car %d%n", System.currentTimeMillis(), carNumber, robot.stepName, line.lineNumber, carNumber);
                Thread.sleep(delays[i]);
                System.out.printf("[%d] (%d) Robot %d, line %d finished working on car %d%n", System.currentTimeMillis(), carNumber, robot.stepName, line.lineNumber, carNumber);
                previousRobot = robot;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("[%d] (%d) Finished processing car %d%n", System.currentTimeMillis(), carNumber, carNumber);
        deliveryTruck.addCar(this);
        previousRobot.lock.unlock();
    }

    public int getCarNumber() {
        return carNumber;
    }

    public static Car createCar(int modelIndex, String color, FactoryLine line, int orderNumber, DeliveryTruck truck, int dealership, FactoryManager manager) {
        Car car = null;
        switch (modelIndex) {
            case 0:
                car = new ModelA(color, line, orderNumber, truck, dealership, manager);
                break;
            case 1:
                car = new ModelB(color, line, orderNumber, truck, dealership, manager);
                break;
            case 2:
                car = new ModelC(color, line, orderNumber, truck, dealership, manager);
                break;
        }
        return car;
    }
}

class ModelA extends Car {
    public ModelA(String color, FactoryLine line, int carNumber, DeliveryTruck deliveryTruck, int dealership, FactoryManager factoryManager) {
        super(color, line, carNumber, deliveryTruck, "ModelA", dealership, factoryManager);
        int[] delays = {100, 200, 100, 100, 100};
        this.delays = delays;
    }
}
class ModelB extends Car {
    public ModelB(String color, FactoryLine line, int carNumber, DeliveryTruck deliveryTruck, int dealership, FactoryManager factoryManager) {
        super(color, line, carNumber, deliveryTruck, "ModelB", dealership, factoryManager);
        int[] delays = {100, 4000, 100, 100, 100};
        this.delays = delays;
    }
}
class ModelC extends Car {
    public ModelC(String color, FactoryLine line, int carNumber, DeliveryTruck deliveryTruck, int dealership, FactoryManager factoryManager) {
        super(color, line, carNumber, deliveryTruck, "ModelC", dealership, factoryManager);
        int[] delays = {500, 500, 500, 500, 500};
        this.delays = delays;
    }
}
