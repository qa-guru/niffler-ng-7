package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {
    private static final Config CFG = Config.getInstance();


    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (username, currency, firstname, surname, full_name, photo, photo_small)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setString(5, user.getFullname());
                ps.setObject(6, user.getPhoto());
                ps.setObject(7, user.getPhotoSmall());

                ps.executeUpdate();

                final UUID generatedKey;

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(rs.getObject("id", UUID.class));
                        userEntity.setUsername(rs.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        userEntity.setFullname(rs.getString("full_name"));
                        userEntity.setFirstname(rs.getString("firstname"));
                        userEntity.setSurname(rs.getString("surname"));
                        userEntity.setPhoto(rs.getBytes("photo"));
                        userEntity.setPhotoSmall(rs.getBytes("photo_small"));

                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ?"
            )) {
                ps.setString(1, username);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(rs.getObject("id", UUID.class));
                        userEntity.setUsername(rs.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        userEntity.setFullname(rs.getString("full_name"));
                        userEntity.setFirstname(rs.getString("firstname"));
                        userEntity.setSurname(rs.getString("surname"));
                        userEntity.setPhoto(rs.getBytes("photo"));
                        userEntity.setPhotoSmall(rs.getBytes("photo_small"));

                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM user WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
