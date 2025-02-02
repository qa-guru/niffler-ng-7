package student.data.dao.impl;

import student.config.Config;
import student.data.dao.UserdataDao;
import student.data.entity.user.UserEntity;
import student.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserdataDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public UserdataUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO user (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, userEntity.getUsername());
            preparedStatement.setString(2, userEntity.getCurrency().name());
            preparedStatement.setString(3, userEntity.getFirstname());
            preparedStatement.setString(4, userEntity.getSurname());
            preparedStatement.setBytes(5, userEntity.getPhoto());
            preparedStatement.setBytes(6, userEntity.getPhotoSmall());
            preparedStatement.setString(7, userEntity.getFullname());
            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Запрос не нашел ключи в БД");
                }
            }
            userEntity.setId(generatedKey);
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserById(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM user WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(resultSet.getObject("id", UUID.class));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    userEntity.setFirstname(resultSet.getString("firstname"));
                    userEntity.setSurname(resultSet.getString("surname"));
                    userEntity.setPhoto(resultSet.getBytes("photo"));
                    userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                    userEntity.setFullname(resultSet.getString("full_name"));
                    return Optional.of(userEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM user WHERE username = ?"
        )) {
            preparedStatement.setObject(1, username);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(resultSet.getObject("id", UUID.class));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    userEntity.setFirstname(resultSet.getString("firstname"));
                    userEntity.setSurname(resultSet.getString("surname"));
                    userEntity.setPhoto(resultSet.getBytes("photo"));
                    userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                    userEntity.setFullname(resultSet.getString("full_name"));
                    return Optional.of(userEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM user WHERE id = ?"
        )) {
            ps.setObject(1, userEntity.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
