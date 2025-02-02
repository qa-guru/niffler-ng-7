package student.data.dao.impl;

import student.config.Config;
import student.data.dao.CategoryDao;
import student.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO category (username, name, archived)"
                        + "VALUES (?,?,?)",
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
                    throw new SQLException("Cant find id in RS");
                }
            }
            category.setId(generatedKey);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findByCategoryId(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from category where id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(
                            categoryEntity
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
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from category where name = ? and username = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, categoryName);
            preparedStatement.setObject(2, username);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(
                            categoryEntity
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
    public List<CategoryEntity> findCategoriesByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from category where username = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                List<CategoryEntity> entities = new ArrayList<>();
                while (resultSet.next()) {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));
                    entities.add(categoryEntity);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategoryById(CategoryEntity category) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from category where id = ?"
        )) {
            preparedStatement.setObject(1, category.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
