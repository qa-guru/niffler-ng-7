package guru.qa.niffler.service.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static guru.qa.niffler.data.DataBases.transaction;

public class CategoryDBClient {

    private static final Config CFG = Config.getInstance();


    public CategoryJson createCategory(CategoryJson categoryJson) {
        return transaction(Connection.TRANSACTION_READ_UNCOMMITTED,
                (Function<Connection, CategoryJson>)  connection ->
                        CategoryJson.fromEntity(new CategoryDAOJdbc(connection)
                                .create(CategoryEntity.fromJson(categoryJson))),
                        CFG.spendJdbcUrl());
    }

    public @Nullable CategoryJson findById(UUID id) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, CategoryJson>) connection ->
                        new CategoryDAOJdbc(connection).findById(id)
                                .map(CategoryJson::fromEntity).orElse(null),
                CFG.spendJdbcUrl());
    }

    public List<CategoryJson> findAllByUsername(String username) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, ? extends List<CategoryJson>>) connection ->
                        new CategoryDAOJdbc(connection).findAllByUsername(username).stream()
                                .map(CategoryJson::fromEntity).toList(),
                CFG.spendJdbcUrl());
    }

    public @Nullable CategoryJson findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, CategoryJson>) connection ->
                        new CategoryDAOJdbc(connection).findByUsernameAndName(username, categoryName)
                                .map(CategoryJson::fromEntity).orElse(null),
                CFG.spendJdbcUrl());
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return transaction(Connection.TRANSACTION_SERIALIZABLE,
                (Function<Connection, CategoryJson>) connection ->
                        CategoryJson.fromEntity(new CategoryDAOJdbc(connection)
                                .update(CategoryEntity.fromJson(categoryJson))),
                CFG.spendJdbcUrl());
    }

    public void delete(CategoryJson categoryJson) {
        transaction(Connection.TRANSACTION_SERIALIZABLE,
                (Consumer<Connection>) connection ->
                        new CategoryDAOJdbc(connection)
                                .delete(CategoryEntity.fromJson(categoryJson)),
                CFG.spendJdbcUrl());
    }
}
