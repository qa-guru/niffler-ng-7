package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.dao.spend.CategoryDAO;
import guru.qa.niffler.data.dao.spend.SpendDAO;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CategoryDBClient {
    private final SpendDAO spendDao = new SpendDAOJdbc();
    private final CategoryDAO categoryDao = new CategoryDAOJdbc();

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return CategoryJson.fromEntity(categoryDao
                .create(CategoryEntity.fromJson(categoryJson)));
    }

    public @Nullable CategoryJson findById(UUID id) {
        return categoryDao.findById(id).map(CategoryJson::fromEntity).orElse(null);
    }

    public List<CategoryJson> findAllByUsername(String username) {
        return categoryDao.findAllByUsername(username).stream().filter(Objects::nonNull)
                .map(CategoryJson::fromEntity).toList();
    }

    public @Nullable CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findByUsernameAndName(username, categoryName)
                .map(CategoryJson::fromEntity).orElse(null);
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return CategoryJson.fromEntity(categoryDao.update(CategoryEntity.fromJson(categoryJson)));
    }

    public void deleteCategory(CategoryJson categoryJson) {
        categoryDao.deleteCategory(CategoryEntity.fromJson(categoryJson));
    }
}
