package training;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private String name;

    private int yearOfBirth;

    public Employee(String name) {
        this.name = name;
    }
}
