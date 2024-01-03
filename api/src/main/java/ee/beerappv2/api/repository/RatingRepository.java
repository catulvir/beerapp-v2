package ee.beerappv2.api.repository;

import ee.beerappv2.api.service.model.Rating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RatingRepository {
    private final JdbcTemplate template;
    public RatingRepository(JdbcTemplate template) {
        this.template = template;
    }

    public Rating saveRating(Long beerId, Long userId, Rating rating) {

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "app_user_id", "beer_id", "username", "rating", "comment", "flavours" };

        parameters.put("app_user_id", userId);
        parameters.put("beer_id", beerId);
        parameters.put("username", rating.getUsername());
        parameters.put("rating", rating.getRating());
        parameters.put("comment", rating.getComment());
        parameters.put("flavours", String.join(",", rating.getFlavours()));

        new SimpleJdbcInsert(template)
                .withTableName("rating")
                .usingColumns(columns)
                .execute(parameters);

        return rating;
    }
}
