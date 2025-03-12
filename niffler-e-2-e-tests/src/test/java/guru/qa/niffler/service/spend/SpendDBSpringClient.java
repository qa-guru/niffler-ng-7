package guru.qa.niffler.service.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOSpringJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDAOSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;

import static guru.qa.niffler.data.DataBases.dataSource;

public class SpendDBSpringClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson create(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDAOSpringJdbc(
                    dataSource(CFG.spendJdbcUrl())).create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(new SpendDAOSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(spendEntity));
    }

    public @Nullable SpendJson findByUsernameAndDescription(String username, String description) {
        SpendEntity spendEntity = new SpendDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findAllByUsernameAndDescription(username, description).orElse(null);
        if (spendEntity != null) {
            new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                    .findById(spendEntity.getCategory().getId())
                    .ifPresent(spendEntity::setCategory);
            return SpendJson.fromEntity(spendEntity);
        }
        return null;
    }

    public void delete(SpendJson spendJson) {
        SpendEntity spendEntity = SpendEntity.fromJson(spendJson);
        CategoryEntity categoryEntity = spendEntity.getCategory();
        new SpendDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .delete(SpendEntity.fromJson(spendJson));
        if (categoryEntity.getId() != null) {
            new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl())).delete(categoryEntity);
        }
    }
}
