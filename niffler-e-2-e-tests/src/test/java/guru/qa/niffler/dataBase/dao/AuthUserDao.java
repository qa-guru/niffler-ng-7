package guru.qa.niffler.dataBase.dao;

import guru.qa.niffler.dataBase.entity.AuthUserEntity;

public interface AuthUserDao {
    AuthUserEntity createUser(AuthUserEntity authUser);
}
