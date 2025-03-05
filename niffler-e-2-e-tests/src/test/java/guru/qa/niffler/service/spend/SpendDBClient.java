package guru.qa.niffler.service.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.DataBases.transaction;


public class SpendDBClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson json) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(json);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDAOJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDAOJdbc(connection).create(spendEntity));
                },
                CFG.spendJdbcUrl());
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
