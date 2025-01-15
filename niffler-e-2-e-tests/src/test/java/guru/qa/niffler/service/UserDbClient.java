package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDAOJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.*;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson userJson) {

        XAFunction<UserJson> xaAuthF = new XAFunction<>(
                connection -> {
                    //1 - создаем запись в табл user, niffler-auth
                    AuthUserEntity aue = new AuthUserDAOJdbc(connection).createUser(AuthUserEntity.fromJson(userJson));
                    //2 - создаем 2 записи read и write в табл authorities, niffler-auth
                    new AuthAuthorityDAOJdbc(connection).createAuthorities(aue);
                    return UserJson.fromAuthEntity(aue);
                },
                CFG.authJdbcUrl());

        XAFunction<UserJson> xaUserDataF = new XAFunction<>(connection -> {
            //3- создаем запись в табл user, niffler-userdata
            UserEntity ue = new UserdataUserDAOJdbc(connection).createUser(UserEntity.fromJson(userJson));
            return UserJson.fromEntity(ue);
        },
                CFG.userdataJdbcUrl());

        return xaTransaction(TRANSACTION_READ_UNCOMMITTED, xaAuthF, xaUserDataF);
    }

    public Optional<UserEntity> findUserByUsername(String username) {

        return transaction(TRANSACTION_READ_UNCOMMITTED, connection -> {
                    Optional<UserEntity> user = new UserdataUserDAOJdbc(connection)
                            .findByUsername(username);
                    return user;
                },
                CFG.userdataJdbcUrl());

    }

}