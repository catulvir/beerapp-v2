package ee.beerappv2.api.service.model;

import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Manufacturer {

    private Long id;

    private List<Beer> beers;

    private String name;

    private String description;

    private String imageLink;
}
