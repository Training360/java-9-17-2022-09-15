package training;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntermediateSamples {

    public static void main(String[] args) {
        List<String> names = List.of("Jane Doe", "Jack Doe", "Joe Doe", "Jack Joe Smith", "Smith");
        // Melyik a legrövidebb keresztnév?

        System.out.println(names.stream()
                .flatMap(s -> getNames(s))
                .collect(Collectors.toList()));
//                .collect(Collectors.toSet()));
    }

    private static Stream<String> getNames(String name) {
        String[] parts = name.split(" ");
        List<String> names = new ArrayList<>(Arrays.asList(parts));
        names.remove(names.size() - 1);
        return names.stream();
    }
}
