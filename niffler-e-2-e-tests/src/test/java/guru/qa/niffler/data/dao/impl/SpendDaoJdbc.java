package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.sql.*;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try(Connection connection = DataBases.connection(CFG.spendJdbcUrl())) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1,  spend.getUsername());
                preparedStatement.setDate(2,  spend.getSpendDate());
                preparedStatement.setString(3,  spend.getCurrency().name());
                preparedStatement.setDouble(4,  spend.getAmount());
                preparedStatement.setString(5,  spend.getDescription());
                preparedStatement.setObject(6, spend.getCategory().getId());

                preparedStatement.executeUpdate();
                final UUID generatedKey;
                try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if(resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                spend.setId(generatedKey);
                return spend;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
