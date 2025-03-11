package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDAO {
    AuthUserEntity create(AuthUserEntity authUserEntity);
    Optional<AuthUserEntity> findById(UUID id);
    Optional<AuthUserEntity> findByUsername(String userEntity);
    void delete(AuthUserEntity authUserEntity);
}
