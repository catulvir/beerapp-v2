package ee.beerappv2.api.controller;

import ee.beerappv2.api.controller.json.RatingJson;
import ee.beerappv2.api.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }
    @PostMapping("/beer/{beerId}/add")
    @PreAuthorize("hasAuthority('user') or hasAuthority('admin')")
    public ResponseEntity<RatingJson> createRating(@PathVariable Long beerId, @RequestBody @Valid RatingJson ratingJson) {
        return ResponseEntity.ok(ratingService.saveRating(beerId, ratingJson.toRating()).toRatingJson());
    }
}
