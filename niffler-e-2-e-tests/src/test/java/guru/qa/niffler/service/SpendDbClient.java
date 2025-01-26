package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public Optional<SpendJson> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(() ->
                        spendDao.findSpendById(id).map(SpendJson::fromEntity),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return jdbcTxTemplate.execute(() -> {
                    List<SpendEntity> allByUsername = spendDao.findAllByUsername(username);
                    return allByUsername.stream().map(SpendJson::fromEntity).collect(Collectors.toList());
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity));
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() ->
                        categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                                .map(CategoryJson::fromEntity),
                Connection.TRANSACTION_SERIALIZABLE
        );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;
                },
                Connection.TRANSACTION_SERIALIZABLE
        );
    }
}
