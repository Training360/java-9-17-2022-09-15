package training.countcollect;

import java.util.List;

public class CounterSample {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(4, 6, -2, 6, -7, 0, 6, -1);

        // Számoljuk meg, a lista hány pozitív, hány negatív számot és hány 0-t tartalmaz!
        System.out.println(new CounterSample().countNumbers(numbers));
        System.out.println(new CounterSample().countNumbers(numbers).getNegatives());
    }

    public Count countNumbers(List<Integer> numbers) {
        return numbers.stream()
                .collect(
                        Count::new,
                        Count::add,
                        Count::add
                );
    }
}
