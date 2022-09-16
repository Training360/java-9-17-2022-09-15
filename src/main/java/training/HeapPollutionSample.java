package training;


import java.util.ArrayList;
import java.util.List;

public class HeapPollutionSample {

    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        // Tudok-e ebbe a listába Integert tenni?

        List rawType = names;

        rawType.add(12);

        System.out.println(names.get(0));
    }
}
