package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.RatingRepository;
import ee.beerappv2.api.service.model.Rating;
import ee.beerappv2.api.service.model.identity.User;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;

    public RatingService(RatingRepository ratingRepository, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
    }

    public Rating saveRating(Long beerId, Rating newRating) {
        User user = userService.findUserByUsername(newRating.getUsername());
        return ratingRepository.saveRating(beerId, user.getId(), newRating);
    }
}
