package training;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
public class ParallelSamples {

    public static void main(String[] args) throws Exception {
        var service = Executors.newFixedThreadPool(2);
        log.info("Start");

//        service.execute(() -> {
//            log.info("Staring");
//            var result = IntStream.rangeClosed(1, 100).sum();
//            log.info("Result: {}", result);
//        });

        var future = service.submit(() -> {
            log.info("Staring");
            var result = IntStream.rangeClosed(1, 100).sum();
            log.info("Result: {}", result);
            return result;
        });

        log.info("Done? {}", future.isDone());

        log.info("Result: " + future.get(5, TimeUnit.SECONDS));

        log.info("Stop");
        service.shutdown();
    }
}
