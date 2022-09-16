package training;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Java9Samples {

    public static void main(String[] args) throws IOException {
//        Map<String, Integer> names = new HashMap<>();
//        names.put("Joe", 2);
//        names.put("Jack", 3);

//        var names = Map.of("Joe", 2, "Jack", 3);
        var names = Map.ofEntries(
                Map.entry("Joe", 2),
                Map.entry("Jack", 3)
        );

        //        List<String> employees = new ArrayList<String>();
//        List<String> employees = new ArrayList<>();
        var employees = new ArrayList<String>();

        Integer i = Integer.valueOf(10);

        Integer i1 = 10;
        Integer i2 = 10;
        System.out.println(i1 == i2);

        i1 = 300;
        i2 = 300;
        System.out.println(i1 == i2);

        String s1 = "alma";
        String s2 = "alma";
        System.out.println(s1 == s2);

        var k = 1; // int
//        k = k + 0.1; NEM FORDUL
        var d = 1.0; // double

        List<String> words = List.of("alma", "korte");
        for (var word: words) {
            System.out.println(word);
        }

        var one = (Supplier<Integer>) () -> 1;

        var text = Files.readString(
//                Paths.get("src/main/resources/employees.csv")
                Path.of("src/main/resources/employees.csv")
        );
        System.out.println(text);
    }
}
