package guru.qa.niffler.mapper;

import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.Authority;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

    private AuthorityEntityRowMapper(){

    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity result = new AuthorityEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setAuthority(Authority.valueOf(rs.getString("authority")));
        result.setUser(rs.getObject("user", AuthUserEntity.class));
        return result;
    }
}
