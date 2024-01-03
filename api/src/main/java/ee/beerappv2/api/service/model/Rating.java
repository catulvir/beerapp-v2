package ee.beerappv2.api.service.model;

import ee.beerappv2.api.controller.json.RatingJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rating {
    private String comment;

    private String username;

    private Float rating;

    private List<String> flavours;

    public RatingJson toRatingJson() {
        return new RatingJson(
            comment = this.getComment(),
            username = this.getUsername(),
            rating = this.getRating(),
            flavours = this.getFlavours()
        );
    }
}
