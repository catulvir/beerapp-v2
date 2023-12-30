package ee.beerappv2.api.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAlreadyExistsException extends Exception {

    private String publicMessage;

    public UserAlreadyExistsException(String string) {
        this.setPublicMessage(string);
    }
}
