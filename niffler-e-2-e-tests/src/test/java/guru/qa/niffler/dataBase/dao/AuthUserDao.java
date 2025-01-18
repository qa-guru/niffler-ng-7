package guru.qa.niffler.dataBase.dao;

import guru.qa.niffler.dataBase.entity.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity createUser(AuthUserEntity authUser);
    Optional<AuthUserEntity> findById(UUID id);
    List<AuthUserEntity> findAll();
}
