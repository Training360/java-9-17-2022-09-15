package training.countgroup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CounterSample {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(4, 6, -2, 6, -7, 0, 6, -1);

        System.out.println(
                numbers.stream().collect(
                        Collectors.groupingBy(
                                NumberType::typeOf, Collectors.counting()
                        ))
        );
    }
}
