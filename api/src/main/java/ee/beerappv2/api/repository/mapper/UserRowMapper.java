package ee.beerappv2.api.repository.mapper;

import ee.beerappv2.api.service.model.identity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class UserRowMapper implements RowMapper<User> {
    @Override
    @Nullable
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setEmail(rs.getString("EMAIL"));
        user.setUsername(rs.getString("USERNAME"));
        user.setId(rs.getLong("ID"));
        user.setPassword(rs.getString("PASSWORD"));
        String roleNames = rs.getString("ROLE_NAMES");
        List<String> roles = (roleNames != null) ? Arrays.stream(roleNames.split(",")).toList() : new ArrayList<>();
        user.setRoles(new HashSet<>(roles));
        return user;
    }
}
