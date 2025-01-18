package guru.qa.niffler.mapper;


import guru.qa.niffler.dataBase.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    private CategoryEntityRowMapper(){

    }

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity result = new CategoryEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setName(rs.getString("name"));
        result.setUsername(rs.getString("username"));
        result.setArchived(rs.getBoolean("archived"));
        return result;
    }
}
