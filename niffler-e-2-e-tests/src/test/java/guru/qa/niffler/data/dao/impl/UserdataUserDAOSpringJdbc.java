package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.jdbc.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserDAOSpringJdbc implements UserdataUserDAO {

    private static final Config config = Config.getInstance();

    @Nonnull
    @Override
    public UserEntity createUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setBytes(5, user.getPhoto());
                    ps.setBytes(6, user.getPhotoSmall());
                    ps.setString(7, user.getFullname());
                    return ps;
                }, keyHolder
        );

        final UUID generatedKey = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");

        user.setId(generatedKey);

        return user;

    }

    @Nonnull
    @Override
    public UserEntity update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        jdbcTemplate.update("""
                                      UPDATE "user"
                                        SET currency    = ?,
                                            firstname   = ?,
                                            surname     = ?,
                                            photo       = ?,
                                            photo_small = ?
                                        WHERE id = ?
                        """,
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getId());

        jdbcTemplate.batchUpdate("""
                                         INSERT INTO friendship (requester_id, addressee_id, status)
                                         VALUES (?, ?, ?)
                                         ON CONFLICT (requester_id, addressee_id)
                                             DO UPDATE SET status = ?
                        """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, user.getId());
                        ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
                        ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
                        ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getFriendshipRequests().size();
                    }
                });
        return user;
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserdataUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE username = ?",
                        UserdataUserEntityRowMapper.instance,
                        username
                )
        );
    }

    @Nonnull
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UserdataUserEntityRowMapper.instance
        );
    }

    @Override
    public void delete(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id = ?",
                user.getId()
        );
    }
}
