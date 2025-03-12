package guru.qa.niffler.service.spend;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.spend.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.DataBases.xaTransaction;


public class SpendDBClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson json) {
        return xaTransaction(Connection.TRANSACTION_READ_UNCOMMITTED,
                new DataBases.XaFunction<>(
                        connection -> {
                            SpendEntity spendEntity = SpendEntity.fromJson(json);
                            if (spendEntity.getCategory().getId() == null) {
                                CategoryEntity categoryEntity = new CategoryDAOJdbc(connection)
                                        .create(spendEntity.getCategory());
                                spendEntity.setCategory(categoryEntity);
                            }
                            return SpendJson.fromEntity(
                                    new SpendDAOJdbc(connection).create(spendEntity));
                        }, CFG.spendJdbcUrl())
        );
    }

    public @Nullable SpendJson findById(UUID id) {
        return xaTransaction(Connection.TRANSACTION_READ_COMMITTED,
                new DataBases.XaFunction<>(connection ->
                        new SpendDAOJdbc(connection).findById(id)
                                .map(SpendJson::fromEntity).orElse(null),
                        CFG.spendJdbcUrl()));
    }

    public List<SpendJson> findAllByUsername(String username) {
        return xaTransaction(Connection.TRANSACTION_READ_COMMITTED,
                new DataBases.XaFunction<>(connection ->
                        new SpendDAOJdbc(connection).findAllByUsername(username).stream()
                                .map(SpendJson::fromEntity).toList(),
                        CFG.spendJdbcUrl()));
    }

    public void deleteSpend(SpendJson spendJson) {
        xaTransaction(Connection.TRANSACTION_SERIALIZABLE,
                new DataBases.XaConsumer<>(
                        connection ->
                                new SpendDAOJdbc(connection)
                                        .delete(SpendEntity.fromJson(spendJson)),
                        CFG.spendJdbcUrl()));
    }


}
