package training;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Java9Samples {

    public static void main(String[] args) {
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

    }
}
