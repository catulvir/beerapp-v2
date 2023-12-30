package ee.beerappv2.api.repository;

import ee.beerappv2.api.controller.json.RegistrationFormJson;
import ee.beerappv2.api.repository.mapper.UserRowMapper;
import ee.beerappv2.api.service.model.identity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final JdbcTemplate template;

    public UserRepository(JdbcTemplate template) {
        this.template = template;
    }

    public void saveUser(RegistrationFormJson registrationForm) {
        Map<String, Object> parameters = new HashMap<>();

        String[] columns = { "email", "password", "username" };

        parameters.put("email", registrationForm.getEmail());
        parameters.put("password", registrationForm.getPassword());
        parameters.put("username", registrationForm.getUsername());

        Number userId = new SimpleJdbcInsert(template)
                .withTableName("app_user")
                .usingGeneratedKeyColumns("id")
                .usingColumns(columns)
                .executeAndReturnKey(parameters);

        template.update("INSERT INTO app_user_role (app_user_id, role_id) VALUES (?, ?)", userId, 2);
    }

    @Nullable
    public User findByEmail(String email) {
        String sql = "SELECT au.ID, au.EMAIL, au.PASSWORD, au.USERNAME, string_agg(r.NAME::VARCHAR, ',') AS ROLE_NAMES FROM APP_USER au " +
                "LEFT JOIN APP_USER_ROLE aur ON aur.app_user_id = au.id " +
                "LEFT JOIN ROLE r ON aur.role_id = r.id " +
                "WHERE au.email = ? GROUP BY au.ID";

        List<User> users = template.query(sql, new UserRowMapper(), email);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Nullable
    public User findByUsername(String username) {
        String sql = "SELECT au.ID, au.EMAIL, au.PASSWORD, au.USERNAME, string_agg(r.NAME::VARCHAR, ',') AS ROLE_NAMES FROM APP_USER au " +
                "LEFT JOIN APP_USER_ROLE aur ON aur.app_user_id = au.id " +
                "LEFT JOIN ROLE r ON aur.role_id = r.id " +
                "WHERE au.username = ? GROUP BY au.ID";

        List<User> users = template.query(sql, new UserRowMapper(), username);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
