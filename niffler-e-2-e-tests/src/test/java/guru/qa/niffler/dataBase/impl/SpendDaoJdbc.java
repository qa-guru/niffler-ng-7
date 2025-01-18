package guru.qa.niffler.dataBase.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dao.SpendDao;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.CategoryEntity;
import guru.qa.niffler.dataBase.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {
    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }


    @Override
    public SpendEntity create(SpendEntity spendEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spendEntity.getUsername());
            ps.setDate(2, spendEntity.getSpendDate());
            ps.setString(3, spendEntity.getCurrency().name());
            ps.setDouble(4, spendEntity.getAmount());
            ps.setString(5, spendEntity.getDescription());
            ps.setObject(6, spendEntity.getCategory().getId());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Запрос не нашел ключи в БД");
                }
            }
            spendEntity.setId(generatedKey);
            return spendEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM spend WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("category_id", UUID.class));
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    se.setCategory(ce);
                    return Optional.of(
                            se
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spendEntities = new ArrayList<>();

        try (Connection connection = DataBases.connection(CFG.spendJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM spend WHERE username = ?"
             )) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("category_id", UUID.class));
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    se.setCategory(ce);
                    spendEntities.add(se);
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching spend data", e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spendEntity) {
        try (Connection connection = DataBases.connection(CFG.spendJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?"
             )) {
            ps.setObject(1, spendEntity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> spendEntities = new ArrayList<>();
        try (Connection connection = DataBases.connection(CFG.spendJDBCUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM spend"
             )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    CategoryEntity ce = new CategoryEntity();
                    ce.setId(rs.getObject("category_id", UUID.class));
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    se.setCategory(ce);
                    spendEntities.add(se);
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching spend data", e);
        }
    }
}
