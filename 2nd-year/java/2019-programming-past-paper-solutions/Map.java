import java.util.*;

public class Map {
    public static void main(String[] args) {
        HashMap<String, Integer> words = new HashMap<>();
        Scanner in = new Scanner(System.in);

        while(in.hasNext()) {
            String word = in.next();
            if(!words.containsKey(nextLine)) {
                words.put(word, 0);
            }
            else {
                int count = words.get(word);
                words.put(word, count + 1);
            }
        }

        for(String word : words.keySet()) {
            System.out.println(word + ": " + words.get(word));
        }
    }
}