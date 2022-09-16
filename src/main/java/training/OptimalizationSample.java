package training;

import java.util.List;
import java.util.stream.Collectors;

public class OptimalizationSample {

    public static void main(String[] args) {
        List<Employee> employees =
                List.of(
                        new Employee(1, "John Doe", 1970),
                        new Employee(2, "Jack Doe", 1980),
                        new Employee(3, "Jane Doe", 1990)
                );

        var ids = employees.stream()
//                .filter(e -> e.getName().startsWith("J")) //
//                .filter(e -> e.getYearOfBirth() >= 1980)

                .filter(e -> e.getName().startsWith("J") && e.getYearOfBirth() >= 1980) // gyorsabb

                .map(Employee::getId)
                .toList(); // Java 16
        System.out.println(ids);
    }
}
