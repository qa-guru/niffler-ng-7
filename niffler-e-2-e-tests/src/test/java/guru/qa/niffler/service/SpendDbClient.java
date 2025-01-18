package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();


    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id).map(SpendJson::fromEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return transaction(connection -> {
                    List<SpendEntity> allByUsername = new SpendDaoJdbc(connection).findAllByUsername(username);
                    return allByUsername.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }
}
