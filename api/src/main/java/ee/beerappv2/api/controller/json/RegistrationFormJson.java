package ee.beerappv2.api.controller.json;

import ee.beerappv2.api.controller.json.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class RegistrationFormJson {
    @Size(min = 4, max = 63)
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 32)
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 32)
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @Email
    @Size(min = 4)
    private String email;
}