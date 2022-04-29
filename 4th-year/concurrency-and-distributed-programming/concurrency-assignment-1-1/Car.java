public class Car {
    int dealershipId;
    int orderId;
    String color;
    String model;

    public Car(int dealershipId, int orderId, String color, String model) {
        this.dealershipId = dealershipId;
        this.orderId = orderId;
        this.color = color;
        this.model = model;
    }

    public int getDealershipId() { return dealershipId; }
    public int getOrderId() { return orderId; }
    public String getModel() {
       return this.model;
    }
}

class CarA extends Car {
    public CarA(int dealershipId, int orderId, String color) {
        super(dealershipId, orderId, color, "A");
    }
}

class CarB extends Car {
    public CarB(int dealershipId, int orderId, String color) {
        super(dealershipId, orderId, color, "B");
    }
}
class CarC extends Car {
    public CarC(int dealershipId, int orderId, String color) {
        super(dealershipId, orderId, color, "C");
    }
}

class CarFactory {
    public Car getCar(int dealershipId, int orderId, String color, String model) {
        switch(model) {
            case "B":
                return new CarB(dealershipId, orderId, color);
            case "C":
                return new CarC(dealershipId, orderId, color);
        }
        return new CarA(dealershipId, orderId, color);
    }
}