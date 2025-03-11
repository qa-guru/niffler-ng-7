package guru.qa.niffler.data.dao.userdata.mapper;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UdUserEntityRowMapper implements RowMapper<UserdataUserEntity> {

    public static final UdUserEntityRowMapper instance = new UdUserEntityRowMapper();

    private UdUserEntityRowMapper() {
    }

    @Override
    public UserdataUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserdataUserEntity result = new UserdataUserEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setFirstname(rs.getString("firstname"));
        result.setSurname(rs.getString("surname"));
        result.setFullname(rs.getString("full_name"));
        result.setPhoto(rs.getBytes("photo"));
        result.setPhotoSmall(rs.getBytes("photo_small"));
        return result;
    }
}

