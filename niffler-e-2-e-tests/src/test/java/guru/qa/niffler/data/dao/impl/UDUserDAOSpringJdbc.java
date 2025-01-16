package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UDUserDAO;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class UDUserDAOSpringJdbc implements UDUserDAO {

    private final DataSource dataSource;

    public UDUserDAOSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setBytes(5, user.getPhoto());
                    ps.setBytes(6, user.getPhotoSmall());
                    ps.setString(7, user.getFullname());
                    return ps;
                },
                kh
        );

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);

        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UdUserEntityRowMapper.INSTANCE,
                        id
                )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {

    }
}
