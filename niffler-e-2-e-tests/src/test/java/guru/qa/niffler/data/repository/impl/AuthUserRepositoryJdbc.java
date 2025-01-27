package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config config = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement userPs = holder(config.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityPs = holder(config.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO authority (user_id, authority) " +
                             "VALUES (?, ?)")) {
            userPs.setString(1, authUserEntity.getUsername());
            userPs.setString(2, pe.encode(authUserEntity.getPassword()));
            userPs.setBoolean(3, authUserEntity.getEnabled());
            userPs.setBoolean(4, authUserEntity.getAccountNonExpired());
            userPs.setBoolean(5, authUserEntity.getAccountNonLocked());
            userPs.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            userPs.executeUpdate();

            final UUID generatedKey;

            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can not find id in ResultSet");
                }
                authUserEntity.setId(generatedKey);

                for (AuthorityEntity authority : authUserEntity.getAuthorities()) {
                    authorityPs.setObject(1, generatedKey);
                    authorityPs.setString(2, authority.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                return authUserEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                "SELECT a.id as authority_id,\n" +
                        "authority,\n" +
                        "user_id as id,\n" +
                        "u.username,\n" +
                        "u.password,\n" +
                        "u.enabled,\n" +
                        "u.account_non_expired,\n" +
                        "u.account_non_locked,\n" +
                        "u.credentials_non_expired\n" +
                        "FROM \"user\" u join public. authority a on u.id = a.user_id\n" +
                        "WHERE u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorityEntities = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(user);
                    ae.setId(rs.getObject("authority_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(ae);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                "SELECT a.id as authority_id,\n" +
                        "authority,\n" +
                        "user_id as id,\n" +
                        "u.username,\n" +
                        "u.password,\n" +
                        "u.enabled,\n" +
                        "u.account_non_expired,\n" +
                        "u.account_non_locked,\n" +
                        "u.credentials_non_expired\n" +
                        "FROM \"user\" u join public. authority a on u.id = a.user_id\n" +
                        "WHERE u.username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorityEntities = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                    }

                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(user);
                    ae.setId(rs.getObject("authority_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorityEntities.add(ae);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                "SELECT a.id as authority_id,\n" +
                        "authority,\n" +
                        "user_id as id,\n" +
                        "u.username,\n" +
                        "u.password,\n" +
                        "u.enabled,\n" +
                        "u.account_non_expired,\n" +
                        "u.account_non_locked,\n" +
                        "u.credentials_non_expired\n" +
                        "FROM \"user\" u join public. authority a on u.id = a.user_id"
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                AuthUserEntity currentUser;
                while (rs.next()) {
                    UUID userId = rs.getObject("id", UUID.class);
                    Optional<AuthUserEntity> existingUser = users.stream().filter(user ->
                            user.getId().equals(userId)).findFirst();
                    if (existingUser.isPresent()) {
                        currentUser = existingUser.get();
                    } else {
                        currentUser = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                        currentUser.setAuthorities(new ArrayList<>());
                        users.add(currentUser);
                    }
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(currentUser);
                    ae.setId(rs.getObject("authority_id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));

                    currentUser.getAuthorities().add(ae);
                }
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
