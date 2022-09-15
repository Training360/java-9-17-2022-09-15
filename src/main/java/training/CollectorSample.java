package training;

import training.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class CollectorSample {

    public static void main(String[] args) {
        List<Employee> employees =
//                new ArrayList<>();
//        employees.add(new Employee("John Doe", 1970));
                // unmodifiable -> mérete nem módosítható
//                Arrays.asList(
//                        new Employee("John Doe", 1970),
//                        new Employee("Jack Doe", 1980),
//                        new Employee("Jane Doe", 1990)
//                );
                // Ez már egy immutable listát ad vissza
                List.of(
                        new Employee("John Doe", 1970),
                        new Employee("Jack Doe", 1980),
                        new Employee("Jane Doe", 1990)
                );

        // Ha módosíthatót akarunk, akkor new ArrayList(List.of(...))

        //employees.add(new Employee("Joe Doe"));
        //employees.set(1, new Employee("Joe Doe", 1980));
        System.out.println(employees);

        Integer sum = employees
                .stream()
                .map(Employee::getYearOfBirth)
                .reduce(
                        0,          // identity
                        (i, j) -> i + j,  // accumulator
                        (i, j) -> i + j   // combiner
                );
//        .mapToInt(i -> i)
//                .sum();

        System.out.println(sum);

        List<Integer> years =
                employees.stream()
                .map(Employee::getYearOfBirth)
                        //.collect(Collectors.toList());
//                                .collect(
//                                        () -> new ArrayList(),
//                                        (List<Integer> oldValue, Integer element) -> oldValue.add(element),
//                                        (List<Integer> one, List<Integer> another) -> one.addAll(another)
//                                );
                        .collect(
                                ArrayList::new,
                                List::add,
                                List::addAll
                        );

        System.out.println(years);
    }
}
