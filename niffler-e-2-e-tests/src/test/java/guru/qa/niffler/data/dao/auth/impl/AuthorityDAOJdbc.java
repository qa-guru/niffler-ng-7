package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthorityDAO;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuthorityDAOJdbc implements AuthorityDAO {

    private Connection connection;

    public AuthorityDAOJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthUserEntity authUser) {
        String query = "INSERT INTO authority (user_id, authority) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                query, PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity authority : authUser.getAuthorities()) {
                preparedStatement.setObject(1, authority.getUser().getId());
                preparedStatement.setString(2, authority.getAuthority().name());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthUserEntity authUserEntity) {
        String query = "DELETE FROM authority WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                query
        )) {
            preparedStatement.setObject(1, authUserEntity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
