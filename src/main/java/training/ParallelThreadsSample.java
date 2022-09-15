package training;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class ParallelThreadsSample {

    public static void main(String[] args) throws Exception {
        var service = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
                log.info("Staring");
                var result = IntStream.rangeClosed(1, 100).sum();

                Thread.sleep(2000);

                log.info("Result: {}", result);
                return result;
        };

        var result1 = service.submit(task);
        var result2 = service.submit(task);

        System.out.println(result1.get(5, TimeUnit.SECONDS));
        System.out.println(result2.get(5, TimeUnit.SECONDS));


    }
}
