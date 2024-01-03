package ee.beerappv2.api.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ee.beerappv2.api.service.model.Manufacturer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ManufacturerRepository {
    private JdbcTemplate template;

    public ManufacturerRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Manufacturer> findManufacturers(String name) {

        StringBuilder sql = new StringBuilder("SELECT * FROM manufacturers ");

        if (name != null) {
            sql.append("WHERE LOWER(name) LIKE ? ORDER BY name");
            String trimmedName = "%" + name.trim().toLowerCase() + "%";
            return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Manufacturer.class), trimmedName);
        }

        sql.append("ORDER BY name");

        return template.query(sql.toString(), BeanPropertyRowMapper.newInstance(Manufacturer.class));
    }

    public Manufacturer findManufacturer(Long id) {

        String sql = "SELECT * FROM manufacturers WHERE id = ?";

        return template.queryForObject(sql, BeanPropertyRowMapper.newInstance(Manufacturer.class), id);
    }

    public Manufacturer saveManufacturer(Manufacturer manufacturer) {

        if (manufacturer.getId() != null) {
            manufacturer.setId(null);
        }

        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "name", "description" };

        parameters.put("name", manufacturer.getName());
        parameters.put("description", manufacturer.getDescription());
        parameters.put("image_link", manufacturer.getImage());

        new SimpleJdbcInsert(template)
                .withTableName("manufacturers")
                .usingColumns(columns)
                .execute(parameters);

        return manufacturer;
    }

    public void updateManufacturer(Manufacturer manufacturer) {

        String sql = "UPDATE manufacturers SET name = ?, description = ?, image_link = ? WHERE id = ?";

        template.update(sql, manufacturer.getName(), manufacturer.getDescription(), manufacturer.getImage(), manufacturer.getId());
    }

    public void deleteManufacturer(Long id) {

        String sql = "DELETE FROM manufacturers WHERE id = ?";

        template.update(sql, id);
    }

    public List<Manufacturer> getAllManufacturers() {
        return template.query("SELECT * FROM manufacturer ORDER BY name", BeanPropertyRowMapper.newInstance(Manufacturer.class));
    }
}

