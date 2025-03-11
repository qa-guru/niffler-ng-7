package guru.qa.niffler.data.dao.auth.impl;


import guru.qa.niffler.data.dao.auth.AuthorityDAO;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorityDAOJdbc implements AuthorityDAO {

    private Connection connection;

    public AuthorityDAOJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity... authority) {
        String query = "DELETE FROM authority WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                query
        )) {
            for (AuthorityEntity entity : authority) {
                preparedStatement.setObject(1, entity.getId());
                preparedStatement.setObject(2, entity.getAuthority().name());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
