package ee.beerappv2.api.controller.json;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class LoginFormJson {
    @Size(max = 63)
    private String username;

    @NotNull
    @NotEmpty
    @Size(max = 32)
    private String password;
}
