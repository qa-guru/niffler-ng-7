package student.service;


import student.config.Config;
import student.data.Databases;
import student.data.dao.impl.CategoryDaoJdbc;
import student.data.dao.impl.SpendDaoJdbc;
import student.data.entity.spend.CategoryEntity;
import student.data.entity.spend.SpendEntity;
import student.model.CategoryJson;
import student.model.SpendJson;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) throws SQLException {
        return Databases.transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            var jdbc = new CategoryDaoJdbc(connection);
            var userCategories = jdbc.findCategoryByUsernameAndCategoryName(spend.username(), spend.category().name());
            if (userCategories.isEmpty()) {
                CategoryEntity categoryEntity = jdbc.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            } else {
                spendEntity.setCategory(userCategories.stream().findFirst().get());
            }
            return SpendJson.fromEntity(
                    new SpendDaoJdbc(connection).createSpend(spendEntity));
        }, CFG.spendJdbcUrl(), null);
    }

    public CategoryJson createCategory(CategoryJson categoryJson) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new CategoryDaoJdbc(connection);
            CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
            return CategoryJson.fromEntity(jdbc.createCategory(categoryEntity));
        }, CFG.spendJdbcUrl(), null);
    }

    public void deleteSpend(SpendJson spendingJson) throws SQLException {
        Databases.transaction(connection -> {
            var jdbc = new SpendDaoJdbc(connection);
            SpendEntity spendEntity = SpendEntity.fromJson(spendingJson);
            jdbc.deleteSpendById(spendEntity);
        }, CFG.spendJdbcUrl(), null);
    }

    public void deleteSpendByCategoryId(UUID id) throws SQLException {
        Databases.transaction(connection -> {
            var jdbc = new SpendDaoJdbc(connection);
            jdbc.deleteSpendByCategoryId(id);
        }, CFG.spendJdbcUrl(), null);
    }

    public void deleteCategory(CategoryJson categoryJson) throws SQLException {
        Databases.transaction(connection -> {
            var jdbc = new CategoryDaoJdbc(connection);
            CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
            jdbc.deleteCategoryById(categoryEntity);
        }, CFG.spendJdbcUrl(), null);
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new CategoryDaoJdbc(connection);
            return jdbc.findByCategoryId(id);
        }, CFG.spendJdbcUrl(), null);
    }

    public Optional<SpendEntity> findSpendById(UUID id) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new SpendDaoJdbc(connection);
            return jdbc.findSpendById(id);
        }, CFG.spendJdbcUrl(), null);
    }

    public List<CategoryEntity> findCategoryByUsername(String username) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new CategoryDaoJdbc(connection);
            return jdbc.findCategoriesByUsername(username);
        }, CFG.spendJdbcUrl(), null);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new CategoryDaoJdbc(connection);
            return jdbc.findCategoryByUsernameAndCategoryName(username, categoryName);
        }, CFG.spendJdbcUrl(), null);
    }

    public List<SpendEntity> findAllSpendsByUserName(String userName) throws SQLException {
        return Databases.transaction(connection -> {
            var jdbc = new SpendDaoJdbc(connection);
            return jdbc.findSpendsByUsername(userName);
        }, CFG.spendJdbcUrl(), null);
    }
}
