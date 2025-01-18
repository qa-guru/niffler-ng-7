package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.AuthorityDao;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;
import guru.qa.niffler.mapper.AuthorityEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorityDaoSpringJdbc implements AuthorityDao {

    private final DataSource dataSource;

    public AuthorityDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AuthorityEntity createUser(AuthorityEntity authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authority.getId());
                        ps.setString(2, authority.getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return 0;
                    }
                }
        );
        return null;
    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM authority",
                AuthorityEntityRowMapper.instance
        );
    }
}
