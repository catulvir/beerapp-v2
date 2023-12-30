package ee.beerappv2.api.controller;

import ee.beerappv2.api.config.security.JwtUtils;
import ee.beerappv2.api.controller.json.JwtJson;
import ee.beerappv2.api.controller.json.LoginFormJson;
import ee.beerappv2.api.controller.json.RegistrationFormJson;
import ee.beerappv2.api.service.UserAlreadyExistsException;
import ee.beerappv2.api.service.AuthService;
import ee.beerappv2.api.service.model.identity.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final AuthService userService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils, AuthService userService) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/user/authorize")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginFormJson loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtJson(jwt, userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/user/registration")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody RegistrationFormJson signupRequest) {
        try {
            signupRequest.setPassword(encoder.encode(signupRequest.getPassword()));
            userService.registerNewUserAccount(signupRequest);
        } catch (UserAlreadyExistsException uaeEx) {
            return ResponseEntity.badRequest().body(Map.of("error", "user_exists", "message", uaeEx.getPublicMessage()));
        }
        return ResponseEntity.ok().build();
    }
}
