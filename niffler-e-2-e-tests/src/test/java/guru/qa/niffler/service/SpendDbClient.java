package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDAOJdbc;
import guru.qa.niffler.data.dao.impl.SpendDAOJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(TRANSACTION_READ_UNCOMMITTED, connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDAOJdbc(connection).createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }

                    return SpendJson.fromEntity(
                            new SpendDAOJdbc(connection).createSpend(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );


    }
}
