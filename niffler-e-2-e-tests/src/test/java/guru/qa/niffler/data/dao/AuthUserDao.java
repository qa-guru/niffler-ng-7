package guru.qa.niffler.data.dao;


import guru.qa.niffler.data.entity.auth.AuthUserEntity;

public interface AuthUserDao {

    AuthUserEntity createUser(AuthUserEntity authUser);
}