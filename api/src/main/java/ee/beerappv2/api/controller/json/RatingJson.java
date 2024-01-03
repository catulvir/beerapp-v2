package ee.beerappv2.api.controller.json;

import ee.beerappv2.api.service.model.Rating;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RatingJson {
    @Size(max = 511)
    @Nullable
    private String comment;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @Min(0)
    @Max(5)
    private Float rating;

    @Size(min = 1)
    private List<String> flavours;

    public Rating toRating() {
        return new Rating(
            comment = this.getComment(),
            username = this.getUsername(),
            rating = this.getRating(),
            flavours = this.getFlavours()
        );
    }
}
