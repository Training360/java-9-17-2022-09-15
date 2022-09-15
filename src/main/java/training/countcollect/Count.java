package training.countcollect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Count {

    private int negatives;

    private int zeros;

    private int positives;


    public void add(int number) {
        if (number < 0) {
            negatives++;
        }
        else if (number == 0) {
            zeros++;
        }
        else {
            positives++;
        }
    }

    public void add(Count another) {
        negatives += another.negatives;
        zeros += another.zeros;
        positives += another.positives;
    }

}
