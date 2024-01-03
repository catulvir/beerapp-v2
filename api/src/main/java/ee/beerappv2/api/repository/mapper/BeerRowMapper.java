package ee.beerappv2.api.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ee.beerappv2.api.service.model.*;
import org.springframework.jdbc.core.RowMapper;
public class BeerRowMapper implements RowMapper<Beer> {

    @Override
    public Beer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Beer beer = new Beer();
        beer.setId(rs.getLong("ID"));
        beer.setName(rs.getString("NAME"));
        // beer.setPriceCategory(rs.getString("PRICE_CATEGORY"));
        // beer.setRoutePath(rs.getString("NAME").trim().toLowerCase().replaceAll("\s", "-"));
        beer.setDescription(rs.getString("DESCRIPTION"));
        beer.setPriceCategory(rs.getString("PRICE_CATEGORY"));
        beer.setImage(rs.getString("IMAGE"));
        beer.setAbv(rs.getFloat("ABV"));
        // beer.setAverageRating(rs.getFloat("AVERAGE_RATING"));
        beer.setBitterness(rs.getFloat("BITTERNESS"));
        beer.setWortDensity(rs.getFloat("WORT_DENSITY"));
        beer.setLowerServeTemperature(rs.getInt("LOWER_SERVE_TEMPERATURE"));
        beer.setHigherServeTemperature(rs.getInt("HIGHER_SERVE_TEMPERATURE"));

        BeerType beerType = new BeerType();
        beerType.setId(rs.getLong("BEER_TYPE_ID"));
        beerType.setName(rs.getString("TYPE_NAME"));
        beerType.setDescription(rs.getString("TYPE_DESCRIPTION"));

        Country originCountry = new Country();
        originCountry.setId(rs.getLong("COUNTRY_ID"));
        originCountry.setName(rs.getString("COUNTRY_NAME"));
        originCountry.setDescription(rs.getString("COUNTRY_DESCRIPTION"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(rs.getLong("MANUFACTURER_ID"));
        manufacturer.setName(rs.getString("MANUFACTURER_NAME"));
        manufacturer.setDescription(rs.getString("MANUFACTURER_DESCRIPTION"));
        manufacturer.setImage(rs.getString("MANUFACTURER_IMAGE"));

        List<Flavour> flavours = new ArrayList<>();
        String flavourIds = rs.getString("FLAVOUR_IDS");
        String flavourNames = rs.getString("FLAVOUR_NAMES");
        String[] flavourIdsArray = (flavourIds != null) ? flavourIds.split(",") : new String[0];
        String[] flavourNamesArray = (flavourNames != null) ? flavourNames.split(",") : new String[0];

        for (int i = 0; i < flavourIdsArray.length; i++) {
            flavours.add(new Flavour(
                    Long.parseLong(flavourIdsArray[i]),
                    flavourNamesArray[i])
            );
        }

        List<Store> stores = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();
        if (rs.getMetaData().getColumnCount() > 22) {
            String storeNames = rs.getString("STORE_NAMES");
            String storeImages = rs.getString("STORE_IMAGES");
            String storeBeerLinks = rs.getString("BEER_LINKS");
            String[] storeNamesArray = (storeNames != null) ? storeNames.split(",") : new String[0];
            String[] storeImagesArray = (storeImages != null && !storeImages.isEmpty()) ? storeImages.split(",") : new String[0];
            String[] storeBeerLinksArray = (storeBeerLinks != null) ? storeBeerLinks.split(",") : new String[0];

            for (int i = 0; i < storeNamesArray.length; i++) {
                stores.add(new Store(
                        storeNamesArray[i],
                        storeImagesArray.length > 0 ? storeImagesArray[i] : null,
                        storeBeerLinksArray[i])
                );
            }

            String ratingUsernames = rs.getString("RATING_USERNAMES");
            String ratingRatings = rs.getString("RATING_RATINGS");
            String ratingComments = rs.getString("RATING_COMMENTS");
            String ratingFlavours = rs.getString("RATING_FLAVOURS");
            String[] ratingUsernamesArray = (ratingUsernames != null) ? ratingUsernames.split(",") : new String[0];
            String[] ratingRatingsArray = (ratingRatings != null) ? ratingRatings.split(",") : new String[0];
            String[] ratingCommentsArray = (ratingComments != null) ? ratingComments.split("_") : new String[0];
            String[] ratingFlavoursArray = (ratingFlavours != null && !ratingFlavours.isEmpty()) ? ratingFlavours.split(";") : new String[0];

            for (int i = 0; i < ratingUsernamesArray.length; i++) {
                ratings.add(new Rating(
                        ratingCommentsArray[i],
                        ratingUsernamesArray[i],
                        Float.parseFloat(ratingRatingsArray[i]),
                        List.of(ratingFlavoursArray[i].split(",")))
                );
            }
        }

        beer.setBeerType(beerType);
        beer.setCountry(originCountry);
        beer.setManufacturer(manufacturer);
        beer.setFlavours(flavours);
        beer.setStores(stores);
        beer.setRatings(ratings);

        return beer;
    }
}
