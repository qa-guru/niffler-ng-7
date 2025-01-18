package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dao.AuthorityDao;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.Authority;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthorityDaoJdbc implements AuthorityDao {

    private static final Config CFG = Config.getInstance();


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

    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        try (Connection connection = DataBases.connection(CFG.authJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM authority"
             )) {
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setId(rs.getObject("id", UUID.class));
                    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(authority);
                }
                return authorityEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching data", e);
        }
    }
}
