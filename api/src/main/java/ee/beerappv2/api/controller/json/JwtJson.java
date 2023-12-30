package ee.beerappv2.api.controller.json;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class JwtJson {

    private String token;

    // private Long id;

    private String username;

    private String email;

    private List<String> roles;
}
