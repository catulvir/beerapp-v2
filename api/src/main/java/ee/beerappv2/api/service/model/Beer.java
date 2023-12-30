package ee.beerappv2.api.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import ee.beerappv2.api.controller.json.BeerJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Beer {

    private Long id;

    private Manufacturer manufacturer;

    private Country country;

    private BeerType beerType;

    private List<Flavour> flavours;

    private String name;

    // private String routePath;

    private String description;

    private String image;

    private Float abv;

    private Float averageRating;

    private Float bitterness;

    private Float wortDensity;

    private Integer lowerServeTemperature;

    private Integer higherServeTemperature;

    private JsonNode nutritionFacts;

    public BeerJson toBeerJson() {
        return new BeerJson(
                id = this.getId(),
                flavours = this.getFlavours(),
                beerType = this.getBeerType(),
                country = this.getCountry(),
                manufacturer = this.getManufacturer(),
                name = this.getName(),
                // routePath = this.getRoutePath(),
                description = this.getDescription(),
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

