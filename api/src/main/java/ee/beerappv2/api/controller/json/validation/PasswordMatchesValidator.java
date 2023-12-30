package ee.beerappv2.api.controller.json.validation;


import ee.beerappv2.api.controller.json.RegistrationFormJson;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegistrationFormJson user = (RegistrationFormJson) obj;
        return user.getPassword() != null && user.getMatchingPassword() != null && user.getPassword().equals(user.getMatchingPassword());
    }
}

