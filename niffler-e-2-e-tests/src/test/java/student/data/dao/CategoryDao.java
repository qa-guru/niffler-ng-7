package student.data.dao;

import student.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findByCategoryId(UUID id);
    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryEntity> findCategoriesByUsername(String username);
    void deleteCategoryById(CategoryEntity category);
}
