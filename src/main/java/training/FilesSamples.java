package training;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesSamples {

    public static void main(String[] args) throws Exception {
//        List<String> lines = Files.readAllLines(Path.of("src/main/resources/employees.csv"));
//        for (var line: lines) {
//            System.out.println(line);
//        }

//        try (BufferedReader reader = Files.newBufferedReader(Path.of("src/main/resources/employees.csv"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        }

        var file = Files.lines(Path.of("src/main/resources/employees.csv"));
        try (file) {
            int sum = file
//                .filter(s -> s.startsWith("1"))
//                .map(s -> s.substring(s.indexOf(",") + 1))
//                .mapToInt(s -> Integer.parseInt(s))
                .map(CsvLine::new)
                    .filter(line -> line.getId() == 1)
                    .mapToInt(CsvLine::getAmount)
                    .sum();
            System.out.println(sum);
        }



    }
}
