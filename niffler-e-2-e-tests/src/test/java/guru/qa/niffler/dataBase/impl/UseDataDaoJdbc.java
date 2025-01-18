package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dao.UseDataDao;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class UseDataDaoJdbc implements UseDataDao {

    private static final Config CFG = Config.getInstance();
    private final Connection connection;

    public UseDataDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
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

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Запрос не нашел ключи в БД");
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
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        ue.setFullname(rs.getString("full_name"));
                        return Optional.of(ue);
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
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username = ?"
            )) {
                ps.setObject(1, username);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity ue = new UserEntity();
                        ue.setId(rs.getObject("id", UUID.class));
                        ue.setUsername(rs.getString("username"));
                        ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        ue.setFirstname(rs.getString("firstname"));
                        ue.setSurname(rs.getString("surname"));
                        ue.setPhoto(rs.getBytes("photo"));
                        ue.setPhotoSmall(rs.getBytes("photo_small"));
                        ue.setFullname(rs.getString("full_name"));
                        return Optional.of(ue);
                    } else {
                        return Optional.empty();
                    }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = DataBases.connection(CFG.userdataJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM \"user\" WHERE id = ?"
             )) {
            ps.setObject(1, user.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> userEntities = new ArrayList<>();
        try (Connection connection = DataBases.connection(CFG.userdataJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM \"user\""
             )) {
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    user.setFirstname(rs.getString("firstname"));
                    user.setSurname(rs.getString("surname"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photoSmall"));
                    user.setFullname(rs.getString("full_name"));
                    userEntities.add(user);
                }
                return userEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching data", e);
        }
    }
}
