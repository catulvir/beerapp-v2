package ee.beerappv2.api.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Store {
    private String name;

    @Nullable
    private String image;

    private String beerLink;
}
