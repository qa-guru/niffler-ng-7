package guru.qa.niffler.dataBase.service;

import guru.qa.niffler.dataBase.dao.CategoryDao;
import guru.qa.niffler.dataBase.dao.SpendDao;
import guru.qa.niffler.dataBase.entity.CategoryEntity;
import guru.qa.niffler.dataBase.entity.SpendEntity;
import guru.qa.niffler.dataBase.impl.CategoryDaoJdbc;
import guru.qa.niffler.dataBase.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendingJson;

import java.util.List;
import java.util.Optional;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendingJson createSpend(SpendingJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendingJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
    }

    public void deleteSpend(SpendingJson spendingJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendingJson);
        spendDao.deleteSpend(spendEntity);
    }

    public void deleteCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        categoryDao.deleteCategory(categoryEntity);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity);
    }

    public List<SpendEntity> findAllSpendsByUserName(String userName) {
        return spendDao.findAllByUsername(userName);
    }
}
