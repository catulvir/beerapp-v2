package ee.beerappv2.api.service;

import ee.beerappv2.api.config.security.JwtUtils;
import ee.beerappv2.api.controller.json.RegistrationFormJson;
import ee.beerappv2.api.repository.UserRepository;
import ee.beerappv2.api.service.model.identity.User;
import ee.beerappv2.api.service.model.identity.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUserAccount(RegistrationFormJson registrationForm) throws UserAlreadyExistsException {
        if (emailExists(registrationForm.getEmail())) {
            logger.error("There is an account with that email address: " + registrationForm.getEmail());
            throw new UserAlreadyExistsException("There is an account with that email address: "
                    + registrationForm.getEmail());
        }

        if (usernameExists(registrationForm.getUsername())) {
            logger.error("There is an account with that username: " + registrationForm.getUsername());
            throw new UserAlreadyExistsException("There is an account with that username: "
                    + registrationForm.getUsername());
        }

        userRepository.saveUser(registrationForm);
    }
    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        return UserDetailsImpl.build(user);
    }
}
