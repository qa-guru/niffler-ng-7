package student.data.dao;

import student.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataDao {

    UserEntity createUser(UserEntity userEntity);

    Optional<UserEntity> findUserById(UUID id);

    Optional<UserEntity> findUserByUsername(String username);

    void deleteUser(UserEntity userEntity);
}
