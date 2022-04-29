public class Main {
    public static void main(String[] args) {
        int arrivalProbability = 50;
        if (args.length > 0) {
            arrivalProbability = Integer.parseInt(args[0]);
        }
        int factoryLines = 3;
        if (args.length > 1) {
            factoryLines = Integer.parseInt(args[1]);
        }
        int tickDuration = 500;
        if (args.length > 2) {
            tickDuration = Integer.parseInt(args[2]);
        }
        int numDealerships = 3;
        if (args.length > 3) {
            numDealerships = Integer.parseInt(args[3]);
        }
        int numOrders = 5;
        if (args.length > 4) {
            numDealerships = Integer.parseInt(args[4]);
        }
        FactoryManager manager = new FactoryManager(arrivalProbability, factoryLines, tickDuration, numDealerships, numOrders);
        manager.start();
    }
}