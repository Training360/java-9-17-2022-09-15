package training.countreduce;

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
                .reduce(
                  new Count(), // identity
                        //(Count oldValue, Integer element) -> oldValue.add(element),// accumulator
                        //(Count one, Count another) -> one.add(another)// combiner
                        Count::add,
                        Count::add
                );
    }
}
