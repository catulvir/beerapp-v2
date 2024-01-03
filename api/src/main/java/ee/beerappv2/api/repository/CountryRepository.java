package ee.beerappv2.api.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.beerappv2.api.service.model.Country;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CountryRepository {
    private final JdbcTemplate template;

    public CountryRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Country> getAllCountries() {
        return template.query("SELECT * FROM country ORDER BY name", BeanPropertyRowMapper.newInstance(Country.class));
    }

    public List<Country> findCountries(String name) {

        StringBuilder sql = new StringBuilder("SELECT * FROM country ");

        if (name != null) {
            sql.append("WHERE LOWER(name) LIKE ? ORDER BY name");
            String trimmedName = "%" + name.trim().toLowerCase() + "%";
            return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Country.class), trimmedName);
        }

        sql.append("ORDER BY name");

        return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Country.class));
    }

    public Country findCountry(Long id) {

        String sql = "SELECT * FROM country WHERE id = ?";

        return template.queryForObject(sql, BeanPropertyRowMapper.newInstance(Country.class), id);
    }

    public Country saveCountry(Country originCountry) {

        if (originCountry.getId() != null) {
            originCountry.setId(null);
        }

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "name", "description" };

        parameters.put("name", originCountry.getName());
        parameters.put("description", originCountry.getDescription());

        new SimpleJdbcInsert(template)
                .withTableName("country")
                .usingColumns(columns)
                .execute(parameters);

        return originCountry;
    }

    public void updateCountry(Country originCountry) {

        if (originCountry.getId() != null) {

            String sql = "UPDATE country SET name = ?, description = ? where id = ?";

            template.update(sql, originCountry.getName(), originCountry.getDescription(), originCountry.getId());
        }
    }

    public void deleteCountry(Long id) {

        String sql = "DELETE FROM country WHERE id = ?";

        template.update(sql, id);
    }
}

