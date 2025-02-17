package guru.qa.niffler.data.dao;


import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserDao {

    AuthUserEntity createUser(AuthUserEntity authUser);

    AuthUserEntity update(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<AuthUserEntity> findByUserName(String username);

    List<AuthUserEntity> findAll();

    void remove(AuthUserEntity authUser);
}