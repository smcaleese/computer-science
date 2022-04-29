public class Factory extends Thread {
    FactoryManager factoryManager;

    public Factory(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;
    }

    public void run() {
        System.out.println("Factory started");
        factoryManager.startProductionLines();
        System.out.println("Production lines started");

        while(true) {
            try {
                factoryManager.addToProductionLine();
            } catch(InterruptedException e) {
                System.out.print(e);
            }
        }
    }
}
