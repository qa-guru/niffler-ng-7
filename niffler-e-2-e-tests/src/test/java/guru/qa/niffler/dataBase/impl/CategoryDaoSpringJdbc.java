package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.dataBase.dao.CategoryDao;
import guru.qa.niffler.dataBase.entity.CategoryEntity;
import guru.qa.niffler.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.mapper.CategoryEntityRowMapper;
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

public class CategoryDaoSpringJdbc implements CategoryDao {

    private final DataSource dataSource;

    public CategoryDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (username, name, archived)" +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3,category.getArchived());
            return ps;
        },kh);

        final UUID generationKey = (UUID) kh.getKeys().get("id");
        category.setId(generationKey);
        return category;
    }

    @Override
    public void update(CategoryEntity category) {

    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE id = ?",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE username = ? AND name = ?",
                        CategoryEntityRowMapper.instance,
                        username,
                        categoryName
                )
        );
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ?",
                CategoryEntityRowMapper.instance,
                username
        );
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "DELETE FROM category WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                       ps.setString(1, String.valueOf(category.getId()));
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                }
        );
    }

    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM category",
                CategoryEntityRowMapper.instance
        );
    }
}
