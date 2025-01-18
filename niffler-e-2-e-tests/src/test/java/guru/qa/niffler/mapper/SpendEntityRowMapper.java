package guru.qa.niffler.mapper;

import guru.qa.niffler.dataBase.entity.*;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper(){

    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity se = new SpendEntity();
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("category_id", UUID.class));
        se.setId(rs.getObject("id", UUID.class));
        se.setUsername(rs.getString("username"));
        se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        se.setSpendDate(rs.getDate("spend_date"));
        se.setAmount(rs.getDouble("amount"));
        se.setDescription(rs.getString("description"));
        se.setCategory(ce);
        return se;
    }
}
