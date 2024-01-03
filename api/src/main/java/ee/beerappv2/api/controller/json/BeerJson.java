package ee.beerappv2.api.controller.json;

import com.fasterxml.jackson.databind.JsonNode;
import ee.beerappv2.api.service.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeerJson {
    private Long id;

    private List<Flavour> flavours;

    private List<Store> stores;

    private List<Rating> ratings;

    private BeerType beerType;

    private Country country;

    private Manufacturer manufacturer;

    @Size(max = 63)
    private String name;

    // private String routePath;

    @Size(max = 511)
    private String description;

    private String priceCategory;

    @Size(max = 511)
    private String image;

    @NotNull
    private Float abv;

    private Float averageRating;

    private Float bitterness;

    private Float wortDensity;

    private Integer lowerServeTemperature;

    private Integer higherServeTemperature;

    private JsonNode nutritionFacts;

    public Beer toBeer() {
        return new Beer(
                id = this.getId(),
                manufacturer = this.getManufacturer(),
                country = this.getCountry(),
                beerType = this.getBeerType(),
                flavours = this.getFlavours(),
                stores = this.getStores(),
                ratings = this.getRatings(),
                name = this.getName(),
                // routePath = this.getRoutePath(),
                description = this.getDescription(),
                priceCategory = this.getPriceCategory(),
                image = this.getImage(),
                abv = this.getAbv(),
                averageRating = this.getAverageRating(),
                bitterness = this.getBitterness(),
                wortDensity = this.getWortDensity(),
                lowerServeTemperature = this.getLowerServeTemperature(),
                higherServeTemperature = this.getHigherServeTemperature(),
                nutritionFacts = this.getNutritionFacts()
        );
    }
}
