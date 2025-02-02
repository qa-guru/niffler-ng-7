package student.data.dao.impl;

import student.config.Config;
import student.data.dao.SpendDao;
import student.data.entity.spend.CategoryEntity;
import student.data.entity.spend.SpendEntity;
import student.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }


    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)"
                        + "VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, spendEntity.getUsername());
            preparedStatement.setDate(2, spendEntity.getSpendDate());
            preparedStatement.setString(3, spendEntity.getCurrency().name());
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, spendEntity.getCategory().getId());

            preparedStatement.executeUpdate();
            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in RS");
                }
            }
            spendEntity.setId(generatedKey);
            return spendEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from spend where id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    SpendEntity spendEntity = new SpendEntity();
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
                    spendEntity.setId(resultSet.getObject("id", UUID.class));
                    spendEntity.setUsername(resultSet.getString("username"));
                    spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                    spendEntity.setAmount(resultSet.getDouble("amount"));
                    spendEntity.setDescription(resultSet.getString("description"));
                    spendEntity.setCategory(categoryEntity);
                    return Optional.of(
                            spendEntity
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findSpendsByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from spend where username = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, username);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                List<SpendEntity> spendEntities = new ArrayList<>();
                while (resultSet.next()) {
                    SpendEntity spendEntity = new SpendEntity();
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
                    spendEntity.setId(resultSet.getObject("id", UUID.class));
                    spendEntity.setUsername(resultSet.getString("username"));
                    spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                    spendEntity.setAmount(resultSet.getDouble("amount"));
                    spendEntity.setDescription(resultSet.getString("description"));
                    spendEntity.setCategory(categoryEntity);
                    spendEntities.add(spendEntity);
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpendById(SpendEntity spendEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from spend where id = ?"
        )) {
            preparedStatement.setObject(1, spendEntity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpendByCategoryId(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from spend where category_id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
