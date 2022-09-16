package training;

import lombok.Data;

@Data
public class CsvLine {

    private long id;

    private int amount;

    public CsvLine(String line) {
        String[] parts = line.split(",");
        id = Long.parseLong(parts[0]);
        amount = Integer.parseInt(parts[1]);
    }
}
