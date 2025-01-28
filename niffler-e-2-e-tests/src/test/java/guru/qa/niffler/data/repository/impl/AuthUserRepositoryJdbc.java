package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config config = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthorityDao authAuthorityDao = new AuthorityDaoJdbc();

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        authUserDao.createUser(authUserEntity);
        authAuthorityDao.createAuthority(authUserEntity.
                getAuthorities().toArray(new AuthorityEntity[0]));
        return authUserEntity;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(config.authJdbcUrl()).connection().prepareStatement(
                """
                        SELECT a.id as authority_id,
                        authority,
                        user_id as id,
                        u.username,
                        u.password,
                        u.enabled,
                        u.account_non_expired,
                        u.account_non_locked,
                        u.credentials_non_expired
                        FROM "user" u
                        JOIN public. authority a
                        ON u.id = a.user_id
                        WHERE u.id = ?"""
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
                """
                        SELECT a.id as authority_id,
                        authority,
                        user_id as id,
                        u.username,
                        u.password,
                        u.enabled,
                        u.account_non_expired,
                        u.account_non_locked,
                        u.credentials_non_expired
                        FROM "user" u
                        JOIN public. authority a
                        ON u.id = a.user_id
                        WHERE u.username = ?"""
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
                """
                        SELECT a.id as authority_id,
                        authority,
                        user_id as id,
                        u.username,
                        u.password,
                        u.enabled,
                        u.account_non_expired,
                        u.account_non_locked,
                        u.credentials_non_expired
                        FROM "user" u
                        JOIN public. authority a
                        ON u.id = a.user_id"""
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
                        Objects.requireNonNull(currentUser).setAuthorities(new ArrayList<>());
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
