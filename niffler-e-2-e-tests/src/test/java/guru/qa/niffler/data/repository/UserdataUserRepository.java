package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    UserEntity update(UserEntity user);

    List<UserEntity> findAll();

    void sendInvitation(UserEntity requester, UserEntity addressee);

    void getInvitation(UserEntity addressee, UserEntity requester);

    void addFriend(UserEntity requester, UserEntity addressee);

    void remove(UserEntity username);
}
