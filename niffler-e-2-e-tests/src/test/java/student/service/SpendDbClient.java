package student.service;


import student.data.dao.CategoryDao;
import student.data.dao.SpendDao;
import student.data.dao.impl.CategoryDaoJdbc;
import student.data.dao.impl.SpendDaoJdbc;
import student.data.entity.spend.CategoryEntity;
import student.data.entity.spend.SpendEntity;
import student.model.CategoryJson;
import student.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        var userCategories = categoryDao.findCategoryByUsernameAndCategoryName(spend.username(), spend.category().name());
        if (userCategories.isEmpty()) {
            CategoryEntity categoryEntity = categoryDao.createCategory(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        } else {
            spendEntity.setCategory(userCategories.stream().findFirst().get());
        }
        return SpendJson.fromEntity(
                spendDao.createSpend(spendEntity));
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        return CategoryJson.fromEntity(categoryDao.createCategory(categoryEntity));
    }

    public void deleteSpend(SpendJson spendingJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendingJson);
        spendDao.deleteSpendById(spendEntity);
    }

    public void deleteSpendByCategoryId(UUID id) {
        spendDao.deleteSpendByCategoryId(id);
    }

    public void deleteCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        categoryDao.deleteCategoryById(categoryEntity);
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findByCategoryId(id);
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    public List<CategoryEntity> findCategoryByUsername(String username) {
        return categoryDao.findCategoriesByUsername(username);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    public List<SpendEntity> findAllSpendsByUserName(String userName) {
        return spendDao.findSpendsByUsername(userName);
    }
}
