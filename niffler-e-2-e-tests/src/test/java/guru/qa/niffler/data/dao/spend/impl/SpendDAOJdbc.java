package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.SpendDAO;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.*;

public class SpendDAOJdbc implements SpendDAO {

    private final Connection connection;

    public SpendDAOJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, spend.getUsername());
            preparedStatement.setDate(2, spend.getSpendDate());
            preparedStatement.setString(3, spend.getCurrency().name());
            preparedStatement.setDouble(4, spend.getAmount());
            preparedStatement.setString(5, spend.getDescription());
            preparedStatement.setObject(6, spend.getCategory().getId());
            preparedStatement.executeUpdate();
            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        String query = "SELECT * FROM spend WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractSpendEntityFromResultSet(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        String query = "SELECT * FROM spend WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<SpendEntity> spendEntities = new ArrayList<>();
                while (resultSet.next()) {
                    spendEntities.add(extractSpendEntityFromResultSet(resultSet));
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        String query = "SELECT * FROM spend";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            try(ResultSet resultSet = ps.executeQuery()) {
                List<SpendEntity> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(extractSpendEntityFromResultSet(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findAllByUsernameAndDescription(String username, String name) {
        return Optional.empty();
    }

    @Override
    public void delete(SpendEntity spend) {
        String query = "DELETE FROM spend WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, spend.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity extractSpendEntityFromResultSet(ResultSet resultSet) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(resultSet.getObject("id", UUID.class));
        spendEntity.setUsername(resultSet.getString("username"));
        spendEntity.setSpendDate(resultSet.getDate("spend_date"));
        spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        spendEntity.setAmount(resultSet.getDouble("amount"));
        spendEntity.setDescription(resultSet.getString("description"));
        spendEntity.setCategory(new CategoryEntity());
        spendEntity.getCategory().setId(resultSet.getObject("category_id", UUID.class));
        return spendEntity;
    }
}
