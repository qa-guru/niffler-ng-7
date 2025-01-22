package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserDAOJdbc implements UserdataUserDAO {

    private static final Config config = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());

            ps.executeUpdate();

            final UUID generateKey;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generateKey = rs.getObject("id", UUID.class);

                } else {
                    throw new SQLException("Can not find id in ResultSet");
                }
            }
            user.setId(generateKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(mapperUserEntity(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(mapperUserEntity(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {

                List<UserEntity> userEntityList = new ArrayList<>();

                while (rs.next()) {
                    userEntityList.add(mapperUserEntity(rs));
                }
                return userEntityList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity mapperUserEntity(ResultSet rs) throws SQLException {
        UserEntity result = new UserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setFirstname(rs.getString("firstname"));
        result.setSurname(rs.getString("surname"));
        result.setPhoto(rs.getBytes("photo"));
        result.setPhotoSmall(rs.getBytes("photo_small"));
        result.setFullname(rs.getString("full_name"));
        return result;
    }

}
