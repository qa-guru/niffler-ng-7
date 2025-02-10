package guru.qa.niffler.data.repository;


import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    AuthUserEntity create(AuthUserEntity user);

    AuthUserEntity update(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    List<AuthUserEntity> findAll();

    Optional<AuthUserEntity> findByUserName(String username);

    void remove(AuthUserEntity user);
}