package guru.qa.niffler.data.dao.userdata;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDAO {
    UserdataUserEntity create(UserdataUserEntity userdataUserEntity);
    Optional<UserdataUserEntity> findById(UUID id);
    Optional<UserdataUserEntity> findByUsername(String username);
    void delete(UserdataUserEntity userdataUserEntity);

}
