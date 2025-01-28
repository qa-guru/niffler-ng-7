package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

    UserEntity createUser(UserEntity user);

    void update(UserEntity user) throws SQLException;

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    void addFriendshipRequest(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);
}
