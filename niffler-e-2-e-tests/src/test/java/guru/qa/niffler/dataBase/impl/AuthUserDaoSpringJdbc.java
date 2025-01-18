package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.AuthUserDao;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.mapper.AuthorityEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private final DataSource dataSource;

    public AuthUserDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUser) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, authUser.getUsername());
            ps.setString(2, authUser.getPassword());
            ps.setBoolean(3,authUser.getEnabled());
            ps.setBoolean(4,authUser.getAccountNonExpired());
            ps.setBoolean(5,authUser.getAccountNonLocked());
            ps.setBoolean(6,authUser.getCredentialsNonExpired());
            return ps;
        },kh);

        final UUID generationKey = (UUID) kh.getKeys().get("id");
        authUser.setId(generationKey);
        return authUser;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        AuthUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                AuthUserEntityRowMapper.instance
        );
    }
}
