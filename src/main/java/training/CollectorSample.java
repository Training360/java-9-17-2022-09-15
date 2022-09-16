package training;

import training.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                        new Employee(1, "John Doe", 1970),
                        new Employee(2, "Jack Doe", 1980),
                        new Employee(3, "Jane Doe", 1990)
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

        List<Integer> yearsWithEmbeddedCollector =
                employees.stream()
                                .map(Employee::getYearOfBirth)
                                        .collect(Collectors.toList());

        System.out.println(years);

        List<String> names = List.of("Jack", "Jane", "John");

        System.out.println(
                names.stream().collect(Collectors.joining("-"))
        );

        System.out.println(
                names.stream()
                        .collect(
                                () -> new StringBuilder(),
                                (StringBuilder sb, String s) -> sb.append("-").append(s),
                                (StringBuilder sb1, StringBuilder sb2) ->  sb1.append(sb2)
                        )
        );

        List<Integer> numbers = List.of(1, 2, 3 ,4);

        System.out.println((numbers.stream().collect(Collectors.summarizingInt(i -> i))));

        //  List<Employee> -> Map<Long, Employee>

        Map<Long, Employee> map = employees.stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity()));
        System.out.println(map);

        Stream<String> ohMy = Stream.of("lions", "tigers", "bears", "tdogs");
        Map<String, Long> histogram = ohMy.collect(Collectors.groupingBy(
                s -> s.substring(0, 1), Collectors.counting()));
        System.out.println(histogram);


    }
}
