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

public class SpendDbClient {

    private static final Config config = Config.getInstance();
    private final JdbcTransactionTemplate jdbcTransactionTemplate = new JdbcTransactionTemplate(
            config.spendJdbcUrl()
    );
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    public SpendJson createSpend(SpendJson spendJson) {

        return jdbcTransactionTemplate.execute(
                () -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(spendDao.create(spendEntity));
                }
        );

    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return jdbcTransactionTemplate.execute(
                () -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
                }
        );
    }

    public void deleteCategory(CategoryJson categoryJson) {
        jdbcTransactionTemplate.execute(
                () -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;
                }
        );
    }

}
