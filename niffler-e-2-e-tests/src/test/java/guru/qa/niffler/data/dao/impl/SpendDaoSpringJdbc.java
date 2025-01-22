package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private static final Config config = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.spendJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, spendEntity.getUsername());
                    ps.setDate(2, spendEntity.getSpendDate());
                    ps.setString(3, spendEntity.getCurrency().name());
                    ps.setDouble(4, spendEntity.getAmount());
                    ps.setString(5, spendEntity.getDescription());
                    ps.setObject(6, spendEntity.getCategory().getId());
                    return ps;
                }, keyHolder
        );

        final UUID generatedKey = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");

        spendEntity.setId(generatedKey);

        return spendEntity;

    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM spend WHERE id = ?",
                        SpendEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance,
                username
        );
    }

    @Override
    public List<SpendEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance
        );
    }

    @Override
    public void deleteSpend(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.spendJdbcUrl()));
        jdbcTemplate.update(
                "DELETE FROM spend WHERE id = ?",
                spendEntity.getId()
        );
    }
}
