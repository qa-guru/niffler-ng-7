package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.spend.CategoryDAO;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDAOJdbc implements CategoryDAO {

    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "INSERT INTO category (username, name, archived) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query, Statement.RETURN_GENERATED_KEYS
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
    public Optional<CategoryEntity> findById(UUID id) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "SELECT * FROM category WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query
            )) {
                preparedStatement.setObject(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(extractCategoryEntityFromResultSet(resultSet));
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
    public Optional<CategoryEntity> findByUsernameAndName(String username, String categoryName) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "SELECT * FROM category WHERE username = ? and name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query
            )) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, categoryName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(extractCategoryEntityFromResultSet(resultSet));
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
    public List<CategoryEntity> findAllByUsername(String username) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "SELECT * FROM category WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query
            )) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<CategoryEntity> categoryEntities = new ArrayList<>();
                    while (resultSet.next()) {
                        categoryEntities.add(extractCategoryEntityFromResultSet(resultSet));
                    }
                    return categoryEntities;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "UPDATE category SET username = ?, name = ?, archived = ? " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query, Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, category.getUsername());
                preparedStatement.setString(2, category.getName());
                preparedStatement.setBoolean(3, category.isArchived());
                preparedStatement.setObject(4, category.getId());
                preparedStatement.executeUpdate();
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        try (Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            String query = "DELETE FROM category WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    query
            )) {
                preparedStatement.setObject(1, category.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CategoryEntity extractCategoryEntityFromResultSet(ResultSet resultSet) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(resultSet.getObject("id", UUID.class));
        categoryEntity.setUsername(resultSet.getString("username"));
        categoryEntity.setName(resultSet.getString("name"));
        categoryEntity.setArchived(resultSet.getBoolean("archived"));
        return categoryEntity;
    }
}