package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {

    public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

    private AuthUserEntityExtractor() {

    }

    @Nullable
    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userCacheMap = new ConcurrentHashMap<>();

        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);

            AuthUserEntity user = userCacheMap.computeIfAbsent(userId, uuid -> {
                AuthUserEntity authUserEntity = new AuthUserEntity();
                authUserEntity.setId(uuid);
                    try {
                        authUserEntity.setUsername(rs.getString("username"));
                        authUserEntity.setPassword(rs.getString("password"));
                        authUserEntity.setEnabled(rs.getBoolean("enabled"));
                        authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                        authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                        authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                return authUserEntity;
            });

            AuthorityEntity authority = new AuthorityEntity();
            authority.setUser(user);
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));

            user.addAuthorities(authority);
        }
        return userCacheMap.get(userId);
    }
}