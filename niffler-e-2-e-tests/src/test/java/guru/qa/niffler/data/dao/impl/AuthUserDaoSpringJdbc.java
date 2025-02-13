package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.jdbc.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private static final Config config = Config.getInstance();

    @Nonnull
    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" " +
                                    "(username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, authUserEntity.getUsername());
                    ps.setString(2, authUserEntity.getPassword());
                    ps.setBoolean(3, authUserEntity.getEnabled());
                    ps.setBoolean(4, authUserEntity.getAccountNonExpired());
                    ps.setBoolean(5, authUserEntity.getAccountNonLocked());
                    ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());
                    return ps;
                }, keyHolder
        );

        final UUID generatedKey = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");

        authUserEntity.setId(generatedKey);

        return authUserEntity;

    }

    @Nonnull
    @Override
    public AuthUserEntity update(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE \"user\" " +
                            "SET username = ?, password = ?, enabled = ?, account_non_expired = ?, " +
                            "account_non_locked = ?, credentials_non_expired = ? " +
                            "WHERE id = ?");
            ps.setString(1, authUserEntity.getUsername());
            ps.setString(2, authUserEntity.getPassword());
            ps.setBoolean(3, authUserEntity.getEnabled());
            ps.setBoolean(4, authUserEntity.getAccountNonExpired());
            ps.setBoolean(5, authUserEntity.getAccountNonLocked());
            ps.setBoolean(6, authUserEntity.getCredentialsNonExpired());
            ps.setObject(7, authUserEntity.getId(), Types.OTHER);
            return ps;
        });
        if (affectedRows == 0) {
            throw new RuntimeException("Updating user failed, no rows affected.");
        }
        return authUserEntity;
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        AuthUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE username = ?",
                        AuthUserEntityRowMapper.instance,
                        username
                )
        );
    }

    @Nonnull
    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                AuthUserEntityRowMapper.instance
        );

    }

    @Override
    public void remove(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
            ps.setObject(1, user.getId(), Types.OTHER);
            return ps;
        });

        if (affectedRows == 0) {
            throw new RuntimeException("Deleting user failed, no rows affected.");
        }
    }

}
