package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.data.dao.spend.SpendDAO;
import guru.qa.niffler.data.dao.spend.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class SpendDAOSpringJdbc implements SpendDAO {

    private final DataSource dataSource;

    public SpendDAOSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        String query = "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, keyHolder);
        UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        String query = "SELECT * FROM spend WHERE id = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return Optional.ofNullable(template.queryForObject(
                    query, SpendEntityRowMapper.instance, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        String query = "SELECT * FROM spend WHERE username = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return template.query(
                    query, SpendEntityRowMapper.instance, username);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<SpendEntity> findAllByUsernameAndDescription(String username, String description) {
        String query = "SELECT * FROM spend WHERE username = ? and description = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return Optional.ofNullable(template.queryForObject(
                    query, SpendEntityRowMapper.instance, username, description));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        String query = "SELECT * FROM spend";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return template.query(query, SpendEntityRowMapper.instance);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void delete(SpendEntity spend) {
        String query = "DELETE FROM spend WHERE id = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setObject(1, spend.getId());
            return ps;
        });
    }
}
