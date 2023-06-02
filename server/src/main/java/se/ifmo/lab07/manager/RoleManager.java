package se.ifmo.lab07.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.dto.Role;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.exception.RoleException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RoleManager {

    private static final Logger logger = LoggerFactory.getLogger(RoleManager.class);

    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String USER = "postgres";

    private static final String PASSWORD = "password";

    private static final String GET_COMMAND_ROLES = """
            SELECT * FROM command_role;
            """;

    private final Map<String, Role> roles;

    private final AuthManager authManager = new AuthManager();

    private RoleManager() {
        this.roles = new HashMap<>();
    }

    private void register(String command, Role role) {
        this.roles.put(command, role);
    }

    public static RoleManager create() {
        var manager = new RoleManager();
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.createStatement();
            var rs = statement.executeQuery(GET_COMMAND_ROLES);
            while (rs.next()) {
                var command = rs.getString(1);
                var role = Role.valueOf(rs.getString(2));
                manager.register(command, role);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return manager;
    }

    public void check(String command, Role role) {
        var commandRole = this.roles.getOrDefault(command, Role.MIDDLE_USER);
        if (role == null || role.weight() < commandRole.weight()) {
            throw new RoleException("Permission denied");
        }
    }

    public Role getUserRole(String username, String password) {
        try {
            return authManager.authorize(username, password).role();
        } catch (AuthorizationException e) {
            return null;
        }
    }

}
