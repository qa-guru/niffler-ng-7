package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private final DataSource dataSource;

    public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void createAuthority(AuthAuthorityEntity... authAuthorityEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authAuthorityEntity[i].getUser_id());
                        ps.setObject(2, authAuthorityEntity[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authAuthorityEntity.length;
                    }
                }
        );
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        return List.of();
    }

    @Override
    public List<AuthAuthorityEntity> findAllByUserId(UUID userId) {
        return List.of();
    }
}
