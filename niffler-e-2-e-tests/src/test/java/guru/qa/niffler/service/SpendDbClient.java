package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDAO;
import guru.qa.niffler.data.dao.SpendDAO;
import guru.qa.niffler.data.dao.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

    private final SpendDAO spendDao = new SpendDAOJdbc();
    private final CategoryDAO categoryDao = new CategoryDAOJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.createCategory(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return SpendJson.fromEntity(
                spendDao.createSpend(spendEntity)
        );
    }
}
