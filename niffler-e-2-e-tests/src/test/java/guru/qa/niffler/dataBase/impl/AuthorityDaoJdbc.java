package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.AuthorityDao;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;

import java.sql.*;
import java.util.UUID;

public class AuthorityDaoJdbc implements AuthorityDao {

    private final Connection connection;

    public AuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }
    @Override
    public AuthorityEntity createUser(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (authority, user_id) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, String.valueOf(authority.getAuthority()));
            ps.setObject(2, authority.getUser().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Запрос не нашел ключи в БД");
                }
                authority.setId(generatedKey);
                return authority;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
