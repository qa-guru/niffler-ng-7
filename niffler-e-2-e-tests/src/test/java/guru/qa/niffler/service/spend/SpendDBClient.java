package guru.qa.niffler.service.spend;

import guru.qa.niffler.data.dao.spend.CategoryDAO;
import guru.qa.niffler.data.dao.spend.SpendDAO;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SpendDBClient {

    private final SpendDAO spendDao = new SpendDAOJdbc();
    private final CategoryDAO categoryDao = new CategoryDAOJdbc();

    public SpendJson createSpend(SpendJson json) {
        SpendEntity spendEntity = SpendEntity.fromJson(json);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity));
    }

    public @Nullable SpendJson findById(UUID id) {
        return spendDao.findById(id).map(SpendJson::fromEntity).orElse(null);
    }

    public List<SpendJson> findAllByUsername(String username) {
        return spendDao.findAllByUsername(username).stream()
                .map(SpendJson::fromEntity).toList();
    }

    public void deleteSpend(SpendJson spendJson) {
        spendDao.deleteSpend(SpendEntity.fromJson(spendJson));
    }


}
