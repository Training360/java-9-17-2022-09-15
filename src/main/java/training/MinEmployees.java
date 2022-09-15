package training;

import java.util.Comparator;
import java.util.List;

public class MinEmployees {

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee(2, "Jack Doe", 1980),
                new Employee(1, "John Doe", 1970),
                new Employee(3, "Jane Doe", 1990)
        );

        // Add vissza a legkorábban született alkalmazottat!

        Employee employee = employees.stream()
                .sorted(Comparator.comparingInt(Employee::getYearOfBirth))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can not use empty collection"));
        System.out.println(employee);

        employee = employees.stream().min(Comparator.comparingInt(Employee::getYearOfBirth))
                .orElseThrow(() -> new IllegalArgumentException("Can not use empty collection"));
        System.out.println(employee);
    }
}
