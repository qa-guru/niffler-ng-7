package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.jdbc.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config config = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthorityDao authAuthorityDao = new AuthorityDaoSpringJdbc();


    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        authUserDao.createUser(authUserEntity);
        authAuthorityDao.createAuthority(authUserEntity.
                getAuthorities().toArray(new AuthorityEntity[0]));
        return authUserEntity;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUserEntity) {
        authUserDao.update(authUserEntity);
        authAuthorityDao.remove(authUserEntity.getAuthorities().getFirst());
        authAuthorityDao.createAuthority(authUserEntity.getAuthorities().toArray(new AuthorityEntity[0]));
        return authUserEntity;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query(
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
                        JOIN public.authority a
                        ON u.id = a.user_id
                        WHERE u.id = ?
                        """,
                AuthUserEntityExtractor.instance,
                id
        ));
    }


    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query(
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
                        JOIN public.authority a
                        ON u.id = a.user_id
                        WHERE u.username = ?""",
                AuthUserEntityExtractor.instance,
                username
        ));
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.authJdbcUrl()));
        Map<UUID, AuthUserEntity> userCacheMap = new ConcurrentHashMap<>();
        jdbcTemplate.query(
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
                        JOIN public.authority a
                        ON u.id = a.user_id""",
                (ResultSet rs) -> {
                    while (rs.next()) {
                        UUID userId = rs.getObject("id", UUID.class);
                        AuthUserEntity user = userCacheMap.computeIfAbsent(userId, uuid -> {
                            try {
                                AuthUserEntity authUserEntity = new AuthUserEntity();
                                authUserEntity.setId(uuid);
                                authUserEntity.setUsername(rs.getString("username"));
                                authUserEntity.setPassword(rs.getString("password"));
                                authUserEntity.setEnabled(rs.getBoolean("enabled"));
                                authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                                authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                                authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                                authUserEntity.setAuthorities(new ArrayList<>());
                                return authUserEntity;
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        AuthorityEntity authority = new AuthorityEntity();
                        authority.setId(rs.getObject("authority_id", UUID.class));
                        authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                        authority.setUser(user);
                        user.getAuthorities().add(authority);
                    }
                });

        return new ArrayList<>(userCacheMap.values());
    }

    @Override
    public void remove(AuthUserEntity user) {
        authAuthorityDao.remove(user.getAuthorities().getFirst());
        authUserDao.remove(user);
    }
}
