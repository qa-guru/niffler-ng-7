package guru.qa.niffler.data.dao.spend.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(rs.getObject("id", UUID.class));
        spendEntity.setUsername(rs.getString("username"));
        spendEntity.setSpendDate(rs.getDate("spend_date"));
        spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        spendEntity.setAmount(rs.getDouble("amount"));
        spendEntity.setDescription(rs.getString("description"));
        spendEntity.setCategory(new CategoryEntity());
        spendEntity.getCategory().setId(rs.getObject("category_id", UUID.class));
        return spendEntity;
    }
}
