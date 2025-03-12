package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.spend.CategoryDAO;
import guru.qa.niffler.data.dao.spend.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class CategoryDAOSpringJdbc implements CategoryDAO {

    private final DataSource dataSource;

    public CategoryDAOSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        String query = "INSERT INTO category (username, name, archived) VALUES (?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, keyHolder);
        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        category.setId(generatedKey);
        return category;
    }

    @Override
    public @Nullable Optional<CategoryEntity> findById(UUID id) {
        String query = "SELECT * FROM category WHERE id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    query, CategoryEntityRowMapper.instance, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nullable Optional<CategoryEntity> findByUsernameAndName(String username, String categoryName) {
        String query = "SELECT * FROM category WHERE username = ? and name = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return Optional.ofNullable(template.queryForObject(
                    query, CategoryEntityRowMapper.instance, username, categoryName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        String query = "SELECT * FROM category WHERE username = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return template.query(query, CategoryEntityRowMapper.instance, username);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CategoryEntity> findAll() {
        String query = "SELECT * FROM category";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try {
            return template.query(query, CategoryEntityRowMapper.instance);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category) {
        String query = "UPDATE category SET username = ?, name = ?, archived = ? WHERE id = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            ps.setObject(4, category.getId());
            return ps;
        });
        return category;
    }

    @Override
    public void delete(CategoryEntity category) {
        String query = "DELETE FROM category WHERE id = ?";
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setObject(1, category.getId());
            return ps;
        });
    }
}