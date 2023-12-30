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
        // beer.setRoutePath(rs.getString("NAME").trim().toLowerCase().replaceAll("\s", "-"));
        beer.setDescription(rs.getString("DESCRIPTION"));
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
        manufacturer.setImageLink(rs.getString("MANUFACTURER_IMAGE"));

        List<Flavour> flavours = new ArrayList<>();
        String flavourIds = rs.getString("FLAVOUR_IDS");
        String flavourNames = rs.getString("FLAVOUR_NAMES");
        String[] ids = (flavourIds != null) ? flavourIds.split(",") : new String[0];
        String[] names = (flavourNames != null) ? flavourNames.split(",") : new String[0];

        for (int i = 0; i < ids.length; i++) {
            flavours.add(new Flavour(Long.parseLong(ids[i]), names[i]));
        }

        beer.setBeerType(beerType);
        beer.setCountry(originCountry);
        beer.setManufacturer(manufacturer);
        beer.setFlavours(flavours);

        return beer;
    }
}
