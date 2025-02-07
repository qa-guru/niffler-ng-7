package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper() {
    }

    @Nonnull
    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity result = new SpendEntity();
        CategoryEntity category = new CategoryEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setSpendDate(rs.getDate("spend_date"));
        result.setAmount(rs.getDouble("amount"));
        result.setDescription(rs.getString("description"));
        category.setId(rs.getObject("category_id", UUID.class));
        result.setCategory(category);
        return result;
    }
}
