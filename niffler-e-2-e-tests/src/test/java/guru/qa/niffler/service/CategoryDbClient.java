package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDAO;
import guru.qa.niffler.data.dao.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.Optional;


public class CategoryDbClient {

    private final CategoryDAO categoryDAO = new CategoryDAOJdbc();

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> category = categoryDAO.findCategoryByUsernameAndCategoryName(username, categoryName);

        return category.map(CategoryJson::fromEntity);
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        CategoryEntity category = categoryDAO.createCategory(
                CategoryEntity.fromJson(categoryJson)
        );
        return CategoryJson.fromEntity(category);
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);

        return CategoryJson.fromEntity(categoryDAO.updateCategory(categoryEntity));
    }
}
