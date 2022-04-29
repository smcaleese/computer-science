import java.util.Map;

interface Variables {
    String[] colors = {"red", "green", "blue"};
    String[] models = {"A", "B", "C"};
    Map<String, Integer> prices = Map.of(
        "A", 10,
        "B", 20,
        "C", 30
    );
}

public class Main {
    static Map<String, Integer> tickRates = Map.of(
        "fast", 1,
        "medium", 5,
        "slow", 20
    );

    public static void main(String[] args) {
        Map<String, Integer> factorySettings = Map.of(
            "tickRate", tickRates.get("fast"),
            "numModels", 3,
            "numColors", 3,
            "numProductionLines", 3,
            "maxTicks", 500,
            "numDealerships", 2
        );
        int[] productionLineSettings = {5, 10, 30, 40, 20};

        FactoryManager factoryManager = new FactoryManager(factorySettings, productionLineSettings);
        factoryManager.start();
    }
}