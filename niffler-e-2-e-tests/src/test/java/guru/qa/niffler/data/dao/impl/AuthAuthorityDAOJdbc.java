package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDAOJdbc implements AuthAuthorityDAO {

    private final Connection connection;

    public AuthAuthorityDAOJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<AuthorityEntity> createUser(AuthUserEntity authUser) {
        List<AuthorityEntity> created = new ArrayList<>();
        List<AuthorityEntity> userAuthorities = authUser.getAuthorities();

        for (AuthorityEntity authority : userAuthorities) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO authority (user_id, authority) " +
                            "VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, authority.getUser().getId());
                ps.setString(2, authority.getAuthority().name());
                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find user id in ResultSet");
                    }
                }

                authority.setId(generatedKey);
                created.add(authority);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return created;
    }
}
