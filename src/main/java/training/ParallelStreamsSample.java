package training;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ParallelStreamsSample {

    public static void main(String[] args) {
        var count = ForkJoinPool.commonPool().getParallelism();
        log.info("Szalak szama: {}", count);

        System.out.println(
                IntStream
                        .rangeClosed(0, 100)
                        .parallel()
                        .sum()
        );


//        // Az ArrayList NEM SZÁLBIZTOS
//        // SOHA DE SOHA DE SOHA DE SOHA NE CSINÁLJUNK ILYENT
//        List<Integer> numbers = //Collections.synchronizedList(new ArrayList<>());
//                new CopyOnWriteArrayList<>();
//
//        Stream.generate(Math::random)
//                .parallel()
//                .map(e ->  (int) (e.doubleValue() * 1000))
//                .limit(10)
//                .forEach(i -> numbers.add(i)); // MELLÉKHATÁS

        int i = 1000;

        // Saját threadpool - és nem a commonPoolon
        ForkJoinPool pool = new ForkJoinPool(4);
        pool.submit(() -> {
            List<Integer> numbers = Stream.generate(Math::random)
                    .parallel()
                    .map(e -> (int) (e.doubleValue() * i))
                    .limit(10)
                    .toList();
            System.out.println(numbers);
        });


    }
}
