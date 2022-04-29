public class Robot extends Thread {
    int delay;
    int productionLineIndex;
    int position;
    FactoryManager factoryManager;

    public Robot(FactoryManager factoryManager, int productionLineIndex, int position, int delay) {
        this.factoryManager = factoryManager;
        this.productionLineIndex = productionLineIndex;
        this.position = position;
        this.delay = delay;
    }

    public void run() {
        while(true) {
            try {
                factoryManager.robotWork(productionLineIndex, position, delay);
            } catch(InterruptedException e) {
                System.out.print(e);
            }
        }
    }
}
