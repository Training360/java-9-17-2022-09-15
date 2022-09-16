package training;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Slf4j
public class CompletableFutureSamples {

    public static void main(String[] args) throws Exception {
        // Létrehozáshoz szükséges metódusok
        log.info("Start");

//        var future = CompletableFuture.runAsync(() -> {
//            int result = IntStream.rangeClosed(0, 10).sum();
//            log.info("Result {}", result);});
//
//        future.get();

        var future = CompletableFuture.supplyAsync(() -> {
            log.info("Calculating");
            return IntStream.rangeClosed(0, 10).sum();
            })
                .thenApplyAsync((result) -> result * 2)
//                .thenAcceptAsync(
//                    result -> log.info("Result: {}", result)
                .whenComplete((result, e) -> {
                    if (e == null) {
                        log.info("Result {}", result);
                    }
                    else {
                        log.error("Can not calculate", e);
                    }
                });


        future.get();

        log.info("End");
    }
}
