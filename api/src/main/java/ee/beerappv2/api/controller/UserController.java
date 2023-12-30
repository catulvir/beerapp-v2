package ee.beerappv2.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user/{username}/ratings")
    public ResponseEntity<?> getRatings(@PathVariable String username) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{username}/ratings")
    public ResponseEntity<?> createRating(@PathVariable String username) {
        return ResponseEntity.ok().build();
    }
}
