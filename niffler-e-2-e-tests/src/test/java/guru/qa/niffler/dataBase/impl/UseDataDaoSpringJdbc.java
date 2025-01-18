package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.UseDataDao;
import guru.qa.niffler.dataBase.entity.UserEntity;
import guru.qa.niffler.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.mapper.UserEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UseDataDaoSpringJdbc implements UseDataDao {

    private final DataSource dataSource;
    public UseDataDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3,user.getFirstname());
            ps.setString(4,user.getSurname());
            ps.setBytes(5,user.getPhoto());
            ps.setBytes(6,user.getPhotoSmall());
            ps.setString(7,user.getFullname());
            return ps;
        },kh);

        final UUID generationKey = (UUID) kh.getKeys().get("id");
        user.setId(generationKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserEntityRowMapper.instance,
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

    @Override
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UserEntityRowMapper.instance
        );
    }
}
