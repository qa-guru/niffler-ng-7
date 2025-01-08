package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface CategoryDAO {

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);
}
