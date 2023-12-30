package ee.beerappv2.api.service.model.identity;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles = new HashSet<>();
}

