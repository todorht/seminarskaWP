package mk.ukim.finki.deals_n_steals.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class SortClass {
    String name;
    String value;

    public SortClass(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
