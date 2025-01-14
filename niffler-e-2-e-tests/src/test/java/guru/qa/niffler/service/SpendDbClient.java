package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public Optional<SpendJson> findSpendById(UUID id) {
        return spendDao.findSpendById(id).map(SpendJson::fromEntity);
    }

    public List<SpendJson> findAllByUsername(String username) {
        List<SpendEntity> allByUsername = spendDao.findAllByUsername(username);
        return allByUsername.stream().map(SpendJson::fromEntity).collect(Collectors.toCollection(ArrayList::new));
    }

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity);
    }

    public void deleteCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.deleteCategory(categoryEntity);
    }
}
