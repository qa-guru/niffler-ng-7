package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class SpendRepositoryJdbc implements SpendRepository {

    private static final Config config = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Nonnull
    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        final UUID categoryId = spendEntity.getCategory().getId();
        if (categoryId == null && categoryDao.findCategoryById(categoryId).isEmpty()) {
            spendEntity.setCategory(
                    categoryDao.create(spendEntity.getCategory())
            );
        }
        return spendDao.create(spendEntity);
    }

    @Nonnull
    @Override
    public SpendEntity update(SpendEntity spend) {
        spendDao.update(spend);
        categoryDao.update(spend.getCategory());
        return spend;
    }

    @Nonnull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity categoryEntity) {
        return null;
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        try (PreparedStatement ps = holder(config.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM category WHERE username = ? and name = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, name);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(
                            CategoryEntityRowMapper.instance.mapRow(rs, rs.getRow())
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findSpendByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(config.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ? and description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(
                            SpendEntityRowMapper.instance.mapRow(rs, rs.getRow())
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Nonnull
    @Override
    public List<SpendEntity> findAllSpendByUsername(String username) {
        return spendDao.findAllByUsername(username);
    }

    @Nonnull
    @Override
    public List<SpendEntity> findAllSpend() {
        return spendDao.findAll();
    }

    @Override
    public void remove(SpendEntity spendEntity) {
        spendDao.deleteSpend(spendEntity);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
