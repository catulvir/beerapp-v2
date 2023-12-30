package ee.beerappv2.api.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.beerappv2.api.service.model.Flavour;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class FlavourRepository {
    private final JdbcTemplate template;

    public FlavourRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Flavour> findFlavours(String name) {

        StringBuilder sql = new StringBuilder("SELECT * FROM flavours ");

        if (name != null) {
            sql.append("WHERE LOWER(name) LIKE ? ORDER BY name");
            String trimmedName = "%" + name.trim().toLowerCase() + "%";
            return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Flavour.class), trimmedName);
        }

        sql.append("ORDER BY name");

        return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Flavour.class));
    }

    public Flavour findFlavour(Long id) {

        String sql = "SELECT * FROM flavours WHERE id = ?";

        return template.queryForObject(sql, BeanPropertyRowMapper.newInstance(Flavour.class), id);
    }

    public Flavour saveFlavour(Flavour flavour) {

        if (flavour.getId() != null) {
            flavour.setId(null);
        }

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "name" };

        parameters.put("name", flavour.getName());

        new SimpleJdbcInsert(template)
                .withTableName("flavours")
                .usingColumns(columns)
                .execute(parameters);

        return flavour;
    }

    public void updateFlavour(Flavour flavour) {

        String sql = "UPDATE flavours SET name = ? WHERE id = ?";

        template.update(sql, flavour.getName(), flavour.getId());
    }

    public void deleteFlavour(Long id) {

        String sql = "DELETE FROM flavours WHERE id = ?";

        template.update(sql, id);
    }
}

