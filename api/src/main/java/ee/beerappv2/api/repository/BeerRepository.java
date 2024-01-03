package ee.beerappv2.api.repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.beerappv2.api.service.model.Beer;
import ee.beerappv2.api.service.model.Flavour;
import ee.beerappv2.api.repository.mapper.BeerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public class BeerRepository {
    private final JdbcTemplate template;

    public BeerRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Beer> findBeers(Map<String, String> searchParams) {

        StringBuilder sql = new StringBuilder(
                "SELECT b.ID, b.BEER_TYPE_ID, b.COUNTRY_ID, b.MANUFACTURER_ID, b.NAME, b.DESCRIPTION, b.PRICE_CATEGORY, b.IMAGE, "
                        +
                        "b.ABV, b.BITTERNESS, b.WORT_DENSITY, " +
                        "b.LOWER_SERVE_TEMPERATURE, b.HIGHER_SERVE_TEMPERATURE, " +
                        "string_agg(f.ID::VARCHAR, ',') AS FLAVOUR_IDS, string_agg(f.NAME, ',') AS FLAVOUR_NAMES, " +
                        "bt.NAME AS TYPE_NAME, bt.DESCRIPTION AS TYPE_DESCRIPTION, " +
                        "oc.NAME AS COUNTRY_NAME, oc.DESCRIPTION AS COUNTRY_DESCRIPTION, " +
                        "m.NAME AS MANUFACTURER_NAME, m.DESCRIPTION AS MANUFACTURER_DESCRIPTION, " +
                        "m.IMAGE AS MANUFACTURER_IMAGE " +
                        "FROM BEER b " +
                        "LEFT JOIN BEER_FLAVOUR bf ON b.ID = bf.BEER_ID " +
                        "LEFT JOIN FLAVOUR f ON f.ID = bf.FLAVOUR_ID " +
                        "LEFT JOIN BEER_TYPE bt ON b.BEER_TYPE_ID = bt.ID " +
                        "LEFT JOIN COUNTRY oc ON b.COUNTRY_ID = oc.ID " +
                        "LEFT JOIN MANUFACTURER m ON b.MANUFACTURER_ID = m.ID ");
        // limit, pagination
        // maybe namedParameterJdbcTemplate

        List<Object> queryParameters = new ArrayList<>();
        if (!searchParams.isEmpty()) {
            sql.append("WHERE ");
        }

        // inject into queryParams breaks sql, but is it even possible?

        for (Map.Entry<String, String> param : searchParams.entrySet()) {
            if (!queryParameters.isEmpty()) {
                sql.append("AND ");
            }
            if (param.getKey().equals("name")) {
                sql.append("LOWER(b.name) LIKE ? ");
                String trimmedName = "%" + param.getValue().trim().toLowerCase() + "%";
                queryParameters.add(trimmedName);
            }
            if (param.getKey().equals("beerTypeName")) {
                sql.append("bt.NAME = ? ");
                queryParameters.add(param.getValue());
            }
            if (param.getKey().equals("originCountryName")) {
                sql.append("oc.NAME = ? ");
                queryParameters.add(param.getValue());
            }
            if (param.getKey().equals("manufacturerName")) {
                sql.append("m.NAME = ? ");
                queryParameters.add(param.getValue());
            }
        }

        sql.append("GROUP BY b.ID, TYPE_NAME, " +
                "TYPE_DESCRIPTION, COUNTRY_NAME, COUNTRY_DESCRIPTION, MANUFACTURER_NAME, " +
                "MANUFACTURER_DESCRIPTION, MANUFACTURER_IMAGE ORDER BY b.name");
        return template.query(sql.toString(), new BeerRowMapper(), queryParameters.toArray());
    }

    @Nullable
    public Beer findBeer(Long id) {

        String sql = "WITH agg_flavour_type_manufacturer_country AS (\n" +
                "    SELECT b.ID, b.BEER_TYPE_ID, b.COUNTRY_ID, b.MANUFACTURER_ID, b.NAME, b.DESCRIPTION, b.PRICE_CATEGORY, b.IMAGE,\n" +
                "           b.ABV, b.BITTERNESS, b.WORT_DENSITY,\n" +
                "           b.LOWER_SERVE_TEMPERATURE, b.HIGHER_SERVE_TEMPERATURE,\n" +
                "           string_agg(f.ID::VARCHAR, ',') AS FLAVOUR_IDS, string_agg(f.NAME, ',') AS FLAVOUR_NAMES,\n" +
                "           bt.NAME AS TYPE_NAME, bt.DESCRIPTION AS TYPE_DESCRIPTION,\n" +
                "           oc.NAME AS COUNTRY_NAME, oc.DESCRIPTION AS COUNTRY_DESCRIPTION,\n" +
                "           m.NAME AS MANUFACTURER_NAME, m.DESCRIPTION AS MANUFACTURER_DESCRIPTION,\n" +
                "           m.IMAGE AS MANUFACTURER_IMAGE\n" +
                "    FROM BEER b\n" +
                "             LEFT JOIN BEER_FLAVOUR bf ON b.ID = bf.BEER_ID\n" +
                "             LEFT JOIN FLAVOUR f ON f.ID = bf.FLAVOUR_ID\n" +
                "             LEFT JOIN BEER_TYPE bt ON b.beer_type_id = bt.id\n" +
                "             LEFT JOIN COUNTRY oc ON b.COUNTRY_ID = oc.ID\n" +
                "             LEFT JOIN MANUFACTURER m ON b.MANUFACTURER_ID = m.ID\n" +
                "    WHERE b.id = ?\n" +
                "    GROUP BY b.ID, TYPE_NAME, TYPE_DESCRIPTION, COUNTRY_NAME, COUNTRY_DESCRIPTION,\n" +
                "             MANUFACTURER_NAME, MANUFACTURER_DESCRIPTION, MANUFACTURER_IMAGE\n" +
                "), agg_rating AS (\n" +
                "    SELECT b.ID,\n" +
                "           string_agg(r.USERNAME, ',') AS RATING_USERNAMES,\n" +
                "           string_agg(r.RATING::VARCHAR, ',') AS RATING_RATINGS,\n" +
                "           string_agg(r.COMMENT, '_') AS RATING_COMMENTS, string_agg(r.FLAVOURS, ';') AS RATING_FLAVOURS\n" +
                "    FROM BEER b\n" +
                "             LEFT JOIN (SELECT beer_id, username, rating, comment, flavours FROM RATING WHERE beer_id = ? GROUP BY beer_id, username, rating, comment, flavours) as r ON b.ID = r.BEER_ID\n" +
                "    WHERE b.id = ?\n" +
                "    GROUP BY b.ID\n" +
                "), agg_store AS (\n" +
                "    SELECT b.ID,\n" +
                "        string_agg(bs.BEER_LINK, ',') AS BEER_LINKS,\n" +
                "        string_agg(s.NAME, ',') as STORE_NAMES,\n" +
                "        string_agg(s.IMAGE, ',') as STORE_IMAGES\n" +
                "    FROM BEER b\n" +
                "        LEFT JOIN BEER_STORE bs ON b.ID = bs.BEER_ID\n" +
                "        LEFT JOIN STORE s ON bs.STORE_ID = s.ID\n" +
                "    WHERE b.id = ?\n" +
                "    GROUP BY b.ID\n" +
                ")\n" +
                "SELECT * FROM agg_flavour_type_manufacturer_country JOIN agg_rating USING (id) JOIN agg_store USING (id)";

        List<Beer> beers = template.query(sql, new BeerRowMapper(), id, id, id, id);
        if (beers.isEmpty()) {
            return null;
        } else {
            return beers.get(0);
        }
    }

    public Beer saveBeer(Beer beer) {

        if (beer.getId() != null) {
            beer.setId(null);
        }

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "beer_type_id", "COUNTRY_ID", "manufacturer_id", "name", "description", "is_craft",
                "abv", "average_rating", "bitterness", "wort_density", "lower_serve_temperature",
                "higher_serve_temperature" };

        parameters.put("beer_type_id", beer.getBeerType().getId());
        parameters.put("COUNTRY_ID", beer.getCountry().getId());
        parameters.put("manufacturer_id", beer.getManufacturer().getId());
        parameters.put("name", beer.getName());
        parameters.put("description", beer.getDescription());
        parameters.put("IMAGE", beer.getImage());
        parameters.put("abv", beer.getAbv());
        parameters.put("bitterness", beer.getBitterness());
        parameters.put("wort_density", beer.getWortDensity());
        parameters.put("lower_serve_temperature", beer.getLowerServeTemperature());
        parameters.put("higher_serve_temperature", beer.getHigherServeTemperature());

        Number beerId = new SimpleJdbcInsert(template)
                .withTableName("beer")
                .usingGeneratedKeyColumns("id")
                .usingColumns(columns)
                .executeAndReturnKey(parameters);

        template.batchUpdate("INSERT INTO beer_flavour (beer_id, flavour_id) " +
                        "VALUES (?, ?)",
                beer.getFlavours(),
                50,
                (PreparedStatement ps, Flavour flavour) -> {
                    ps.setLong(1, beerId.longValue());
                    ps.setLong(2, flavour.getId());
                });

        return beer;
    }

    public void updateBeer(Beer beer) {

        String updateSql = "UPDATE beer SET beer_type_id = ?, COUNTRY_ID = ?, manufacturer_id = ?, name = ?," +
                "description = ?, IMAGE = ?, is_craft = ?, abv = ?, average_rating = ?, bitterness = ?,"
                +
                "wort_density = ?, lower_serve_temperature = ?, higher_serve_temperature = ? WHERE id = ?";

        template.update(updateSql, beer.getBeerType().getId(), beer.getCountry().getId(),
                beer.getManufacturer().getId(), beer.getName(), beer.getDescription(), beer.getImage(), beer.getAbv(),
                beer.getAverageRating(), beer.getBitterness(), beer.getWortDensity(),
                beer.getLowerServeTemperature(), beer.getHigherServeTemperature(), beer.getId());

        // maybe compare flavours not to uselessly run batch and delete every time

        String deleteFlavoursSql = "DELETE FROM beer_flavour WHERE beer_id = ?";

        template.update(deleteFlavoursSql, beer.getId());

        template.batchUpdate("INSERT INTO beer_flavour (beer_id, flavour_id) " +
                        "VALUES (?, ?)",
                beer.getFlavours(),
                50,
                (PreparedStatement ps, Flavour flavour) -> {
                    ps.setLong(1, beer.getId());
                    ps.setLong(2, flavour.getId());
                });
    }

    public void deleteBeer(Long id) {

        String sql = "DELETE FROM beer WHERE id = ?";

        template.update(sql, id);
    }

    @Nullable
    public List<Beer> findUserRatedBeers(String username) {
        String sql = "WITH agg_flavour_type_manufacturer_country AS (\n" +
                "    SELECT b.ID, b.BEER_TYPE_ID, b.COUNTRY_ID, b.MANUFACTURER_ID, b.NAME, b.DESCRIPTION, b.PRICE_CATEGORY, b.IMAGE,\n" +
                "           b.ABV, b.BITTERNESS, b.WORT_DENSITY,\n" +
                "           b.LOWER_SERVE_TEMPERATURE, b.HIGHER_SERVE_TEMPERATURE,\n" +
                "           string_agg(f.ID::VARCHAR, ',') AS FLAVOUR_IDS, string_agg(f.NAME, ',') AS FLAVOUR_NAMES,\n" +
                "           bt.NAME AS TYPE_NAME, bt.DESCRIPTION AS TYPE_DESCRIPTION,\n" +
                "           oc.NAME AS COUNTRY_NAME, oc.DESCRIPTION AS COUNTRY_DESCRIPTION,\n" +
                "           m.NAME AS MANUFACTURER_NAME, m.DESCRIPTION AS MANUFACTURER_DESCRIPTION,\n" +
                "           m.IMAGE AS MANUFACTURER_IMAGE\n" +
                "    FROM BEER b\n" +
                "             LEFT JOIN BEER_FLAVOUR bf ON b.ID = bf.BEER_ID\n" +
                "             LEFT JOIN FLAVOUR f ON f.ID = bf.FLAVOUR_ID\n" +
                "             LEFT JOIN BEER_TYPE bt ON b.beer_type_id = bt.id\n" +
                "             LEFT JOIN COUNTRY oc ON b.COUNTRY_ID = oc.ID\n" +
                "             LEFT JOIN MANUFACTURER m ON b.MANUFACTURER_ID = m.ID\n" +
                "    GROUP BY b.ID, TYPE_NAME, TYPE_DESCRIPTION, COUNTRY_NAME, COUNTRY_DESCRIPTION,\n" +
                "             MANUFACTURER_NAME, MANUFACTURER_DESCRIPTION, MANUFACTURER_IMAGE\n" +
                "), agg_rating AS (\n" +
                "    SELECT b.ID,\n" +
                "           string_agg(r.USERNAME, ',') AS RATING_USERNAMES,\n" +
                "           string_agg(r.RATING::VARCHAR, ',') AS RATING_RATINGS,\n" +
                "           string_agg(r.COMMENT, '_') AS RATING_COMMENTS, string_agg(r.FLAVOURS, ';') AS RATING_FLAVOURS\n" +
                "    FROM BEER b\n" +
                "             LEFT JOIN (SELECT beer_id, username, rating, comment, flavours FROM RATING WHERE username = ? GROUP BY beer_id, username, rating, comment, flavours) as r ON b.ID = r.BEER_ID\n" +
                "    WHERE r.username = ?\n" +
                "    GROUP BY b.ID\n" +
                "), agg_store AS (\n" +
                "    SELECT b.ID,\n" +
                "        string_agg(bs.BEER_LINK, ',') AS BEER_LINKS,\n" +
                "        string_agg(s.NAME, ',') as STORE_NAMES,\n" +
                "        string_agg(s.IMAGE, ',') as STORE_IMAGES\n" +
                "    FROM BEER b\n" +
                "        LEFT JOIN BEER_STORE bs ON b.ID = bs.BEER_ID\n" +
                "        LEFT JOIN STORE s ON bs.STORE_ID = s.ID\n" +
                "    GROUP BY b.ID\n" +
                ")\n" +
                "SELECT * FROM agg_flavour_type_manufacturer_country JOIN agg_rating USING (id) JOIN agg_store USING (id)";

        return template.query(sql, new BeerRowMapper(), username, username);
    }
}
