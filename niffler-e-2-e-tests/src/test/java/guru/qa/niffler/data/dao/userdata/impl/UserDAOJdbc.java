package guru.qa.niffler.data.dao.userdata.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.userdata.UserDAO;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDAOJdbc implements UserDAO {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity userEntity) {
        try (Connection connection = DataBases.connection(CFG.userdataJdbcUrl())) {
            String query = "INSERT INTO user (username, currency) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query, Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, userEntity.getUsername());
                preparedStatement.setString(2, userEntity.getCurrency().name());

                preparedStatement.executeUpdate();
                final UUID generatedKey;
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                userEntity.setId(generatedKey);
                return userEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try(Connection connection = DataBases.connection(CFG.userdataJdbcUrl())) {
            String query = "SELECT * FROM 'user' WHERE id = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, id);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        UserEntity userEntity = extractUserEntityFromResultSet(resultSet);
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
    public List<UserEntity> findByUsername(String username) {
        try (Connection connection = DataBases.connection(CFG.userdataJdbcUrl())) {
            String query = "SELECT * FROM 'user' WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, username);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<UserEntity> userEntities = new ArrayList<>();
                    while (resultSet.next()) {
                        userEntities.add(extractUserEntityFromResultSet(resultSet));
                    }
                    return userEntities;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity userEntity) {
        try (Connection connection = DataBases.connection(CFG.userdataJdbcUrl())) {
            String query = "DELETE FROM 'user' WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setObject(1, userEntity.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private UserEntity extractUserEntityFromResultSet(ResultSet resultSet) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
        userEntity.setFullname(resultSet.getString("full_name"));
        return userEntity;
    }
}
