package guru.qa.niffler.service.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.DataBases.dataSource;

public class CategoryDBSpringClient {

    private static final Config CFG = Config.getInstance();

    public CategoryJson create(CategoryJson categoryJson) {
        return CategoryJson.fromEntity(new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .create(CategoryEntity.fromJson(categoryJson)));
    }

    public @Nullable CategoryJson findById(UUID id) {
        return new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findById(id).map(CategoryJson::fromEntity).orElse(null);
    }

    public @Nullable CategoryJson findByUsernameAndName(String username, String categoryName) {
        return new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findByUsernameAndName(username, categoryName).map(CategoryJson::fromEntity).orElse(null);
    }

    public List<CategoryJson> findAll() {
        return new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findAll().stream().map(CategoryJson::fromEntity).toList();
    }

    public void delete(CategoryJson categoryJson) {
        new CategoryDAOSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .delete(CategoryEntity.fromJson(categoryJson));
    }
}
