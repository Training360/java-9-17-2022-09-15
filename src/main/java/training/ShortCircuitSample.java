package training;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ShortCircuitSample {

    public static void main(String[] args) {

        // Stream.generate(() -> Math.random())
        Stream.generate(Math::random)
                .limit(10)
                //.forEach(i -> System.out.println(i));
                .forEach(i -> log.info(Double.toString(i)));

        // Limit esetén nem hozza létre a employee-kat 5-10-ig
        IntStream
                .range(0, 10)
                //.mapToObj(ShortCircuitSample::createEmployee)
                .mapToObj(i -> "John " + i)
                .map(Employee::new)
                .limit(5) // Short-circuiting
                //.forEach(e -> System.out.println(e)); // NE használjunk lambda kifejezéseket, csak method reference-t
                //.forEach(System.out::println);
//                .forEach(e -> log.info(e.toString()));
                .map(Employee::toString)
                .forEach(log::info);


        // Lazy kiértékelés: amíg nincs lezáró művelet, addig nem indul be a stream
        IntStream
                .range(0, 10)
                .mapToObj(i -> {
                    log.info("Create employee xxx");
                    return new Employee(1, "John " + i, 1970);});

    }

    private static Employee createEmployee(int i) {
        log.info("Create employee");
        return new Employee(2, "John " + i, 1970);
    }
}
