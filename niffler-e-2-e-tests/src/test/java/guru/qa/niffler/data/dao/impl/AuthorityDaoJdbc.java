package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthorityDaoJdbc implements AuthorityDao {

    private static final Config config = Config.getInstance();


    @Override
    public void createAuthority(AuthorityEntity... authAuthorityEntities) {

        try (
                PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                        "INSERT INTO authority (user_id, authority) " +
                                "VALUES (?, ?)"
                )) {
            for (AuthorityEntity authority : authAuthorityEntities) {
                ps.setObject(1, authority.getId());
                ps.setString(2, authority.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Nonnull
    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority")) {
            ps.execute();
            List<AuthorityEntity> result = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    result.add(mapperAuthAuthorityEntity(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public List<AuthorityEntity> findAllByUserId(UUID userId) {
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority where user_id = ?")) {
            ps.setObject(1, userId);
            ps.execute();
            List<AuthorityEntity> result = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    result.add(mapperAuthAuthorityEntity(rs));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(AuthorityEntity authorityEntity) {
        try (PreparedStatement deletePs = holder(config.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM authority WHERE user_id = ?")) {
            deletePs.setObject(1, authorityEntity.getUser().getId());
            deletePs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private AuthorityEntity mapperAuthAuthorityEntity(ResultSet rs) throws SQLException {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setId(rs.getObject("id", UUID.class));
        ae.getUser().setId(rs.getObject("user_id", UUID.class));
        ae.setAuthority(Authority.valueOf(rs.getString("authority")));
        return ae;
    }
}
