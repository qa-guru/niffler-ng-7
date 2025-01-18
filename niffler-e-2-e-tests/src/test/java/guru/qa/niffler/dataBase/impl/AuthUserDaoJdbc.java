package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dao.AuthUserDao;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.Authority;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUser) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, authUser.getUsername());
            ps.setString(2, authUser.getPassword());
            ps.setBoolean(3, authUser.getEnabled());
            ps.setBoolean(4, authUser.getAccountNonExpired());
            ps.setBoolean(5, authUser.getAccountNonLocked());
            ps.setBoolean(6, authUser.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Запрос не нашел ключи в БД");
                }
                authUser.setId(generatedKey);
                return authUser;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> authUserEntities = new ArrayList<>();
        try (Connection connection = DataBases.connection(CFG.authJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM \"user\""
             )) {
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    authUserEntity.setId(rs.getObject("id", UUID.class));
                    authUserEntity.setPassword(rs.getString("password"));
                    authUserEntity.setEnabled(rs.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    authUserEntities.add(authUserEntity);
                }
                return authUserEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching data", e);
        }
    }
}
