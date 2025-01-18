package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.SpendDao;
import guru.qa.niffler.dataBase.entity.SpendEntity;
import guru.qa.niffler.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.mapper.SpendEntityRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private final DataSource dataSource;

    public SpendDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public SpendEntity create(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
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
        }, kh);

        final UUID generationKey = (UUID) kh.getKeys().get("id");
        spendEntity.setId(generationKey);
        return spendEntity;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE id = ?",
                SpendEntityRowMapper.instance,
                username
        );
    }

    @Override
    public void deleteSpend(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "DELETE FROM spend WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, String.valueOf(spendEntity.getId()));
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                }
        );
    }

    @Override
    public List<SpendEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM spend",
                SpendEntityRowMapper.instance
        );
    }
}
