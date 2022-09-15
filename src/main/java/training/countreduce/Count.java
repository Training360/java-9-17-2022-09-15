package training.countreduce;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class Count {

    int negatives;

    int zeros;

    int positives;

    public Count() {
        negatives = 0;
        zeros = 0;
        positives = 0;
    }

    public Count add(int number) {
        if (number < 0) {
            return new Count(negatives + 1, zeros, positives);
        }
        else if (number == 0) {
            return new Count(negatives, zeros + 1, positives);
        }
        else {
            return new Count(negatives, zeros, positives + 1);
        }
    }

    public Count add(Count another) {
        return new Count(negatives + another.negatives,
                zeros + another.zeros,
                positives + another.positives);
    }

}
