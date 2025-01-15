package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;


public class CategoryDbClient {

    private static final Config CFG = Config.getInstance();

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(TRANSACTION_READ_UNCOMMITTED, connection -> {
                    Optional<CategoryEntity> category = new CategoryDAOJdbc(connection)
                            .findCategoryByUsernameAndCategoryName(username, categoryName);
                    return category.map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return transaction(TRANSACTION_READ_UNCOMMITTED, connection -> {
                    CategoryEntity category = new CategoryDAOJdbc(connection).createCategory(
                            CategoryEntity.fromJson(categoryJson)
                    );
                    return CategoryJson.fromEntity(category);
                },
                CFG.spendJdbcUrl()
        );

    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return transaction(TRANSACTION_READ_UNCOMMITTED, connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(new CategoryDAOJdbc(connection).updateCategory(categoryEntity));
                },
                CFG.spendJdbcUrl()
        );
    }
}
