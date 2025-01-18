package guru.qa.niffler.dataBase.dao;

import guru.qa.niffler.dataBase.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UseDataDao {

    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void delete(UserEntity user);

    List<UserEntity> findAll();
}
