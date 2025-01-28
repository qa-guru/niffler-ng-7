package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    SpendEntity createSpend(SpendEntity spendEntity);

    SpendEntity update(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name);

    Optional<SpendEntity> findSpendByUsernameAndSpendDescription(String username, String description);

    Optional<SpendEntity> findSpendById(UUID id);

    List<SpendEntity> findAllSpendByUsername(String username);

    List<SpendEntity> findAllSpend();

    void deleteSpend(SpendEntity spendEntity);

    void deleteCategory(CategoryEntity category);
}
