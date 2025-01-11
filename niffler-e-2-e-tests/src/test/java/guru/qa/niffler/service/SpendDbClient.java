package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

    private static final Config config = Config.getInstance();

    public SpendJson createSpend(SpendJson spendJson) {

        return Databases.transaction(
                connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
                },
                config.spendJdbcUrl()
        );

    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return Databases.transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                config.spendJdbcUrl()
        );
    }

    public void deleteCategory(CategoryJson categoryJson) {
        Databases.transaction(
                connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                config.spendJdbcUrl()
        );
    }

}
