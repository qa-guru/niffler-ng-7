package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, category.getUsername());
                preparedStatement.setString(2, category.getName());
                preparedStatement.setBoolean(3, category.isArchived());

                preparedStatement.executeUpdate();
                final UUID generatedKey;
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                category.setId(generatedKey);
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM category WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setObject(1, id);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        CategoryEntity category = new CategoryEntity();
                        category.setId(resultSet.getObject("id", UUID.class));
                        category.setUsername(resultSet.getString("username"));
                        category.setName(resultSet.getString("name"));
                        category.setArchived(resultSet.getBoolean("archived"));
                        return Optional.of(category);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}