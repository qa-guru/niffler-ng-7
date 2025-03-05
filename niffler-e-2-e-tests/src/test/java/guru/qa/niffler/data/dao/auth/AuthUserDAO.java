package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;

public interface AuthUserDAO {
    AuthUserEntity create(AuthUserEntity authUserEntity);
    Optional<AuthUserEntity> findByUsername(String userEntity);
    void delete(AuthUserEntity authUserEntity);
}
