package guru.qa.niffler.dataBase.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.CategoryEntity;
import guru.qa.niffler.dataBase.entity.SpendEntity;
import guru.qa.niffler.dataBase.impl.CategoryDaoJdbc;
import guru.qa.niffler.dataBase.impl.SpendDaoJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendingJson;

import java.util.List;
import java.util.Optional;

public class SpendDbClient {


    private static final Config CFG = Config.getInstance();

    public SpendingJson createSpend(SpendingJson spendJson) {
        return DataBases.transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendingJson.fromEntity(
                  new SpendDaoJdbc(connection).create(spendEntity)
            );
        },CFG.spendJDBCUrl(),0);
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return DataBases.transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
            return CategoryJson.fromEntity(
                    new CategoryDaoJdbc(connection).create(categoryEntity)
            );
        },CFG.spendJDBCUrl(),0);
    }

    public void deleteSpend(SpendingJson spendingJson) {
         DataBases.transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spendingJson);
            new SpendDaoJdbc(connection).deleteSpend(spendEntity);
        },CFG.spendJDBCUrl(),0);
    }

    public void deleteCategory(CategoryJson categoryJson) {
        DataBases.transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
            new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
        },CFG.spendJDBCUrl(),0);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return DataBases.transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username,categoryName)
                    .map(CategoryJson::fromEntity);
        },CFG.spendJDBCUrl(),0);
    }

    public List<SpendEntity> findAllSpendsByUserName(String userName) {
        return DataBases.transaction(connection -> {
            return new SpendDaoJdbc(connection).findAllByUsername(userName);
        },CFG.spendJDBCUrl(),0);
    }
}
