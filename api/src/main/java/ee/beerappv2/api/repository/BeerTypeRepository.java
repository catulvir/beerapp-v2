package ee.beerappv2.api.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.beerappv2.api.service.model.BeerType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class BeerTypeRepository {
    private final JdbcTemplate template;

    public BeerTypeRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<BeerType> findBeerTypes(String name) {

        StringBuilder sql = new StringBuilder("SELECT * FROM beer_types ");

        if (name != null) {
            sql.append("WHERE LOWER(name) LIKE ? ORDER BY name");
            String trimmedName = "%" + name.trim().toLowerCase() + "%";
            return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(BeerType.class), trimmedName);
        }

        sql.append("ORDER BY name");

        return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(BeerType.class));
    }

    public BeerType findBeerType(Long id) {

        String sql = "SELECT * FROM beer_types WHERE id = ?";

        return template.queryForObject(sql, BeanPropertyRowMapper.newInstance(BeerType.class), id);
    }

    public BeerType saveBeerType(BeerType beerType) {

        if (beerType.getId() != null) {
            beerType.setId(null);
        }

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "name", "description" };

        parameters.put("name", beerType.getName());
        parameters.put("description", beerType.getDescription());

        new SimpleJdbcInsert(template)
                .withTableName("beer_types")
                .usingColumns(columns)
                .execute(parameters);

        return beerType;
    }

    public void updateBeerType(BeerType beerType) {

        String sql = "UPDATE beer_types SET name = ?, description = ? WHERE id = ?";

        template.update(sql, beerType.getName(), beerType.getDescription(), beerType.getId());
    }

    public void deleteBeerType(Long id) {

        String sql = "DELETE FROM beer_types WHERE id = ?";

        template.update(sql, id);
    }
}
