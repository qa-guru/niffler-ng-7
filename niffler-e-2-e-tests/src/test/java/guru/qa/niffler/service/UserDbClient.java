package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.data.Databases.XaFunction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public Record createUser(AuthUserJson authUser, UserJson userJson) {
        return xaTransaction(
                new XaFunction<>(
                        connection -> {
                            AuthUserEntity auth = AuthUserEntity.fromJson(authUser);
                            return AuthUserJson.fromEntity(
                                    new AuthUserDaoJdbc(connection).createUser(auth));
                        },
                        CFG.authJdbcUrl()),
                new XaFunction<>(
                        connection -> {
                            UserEntity user = UserEntity.fromJson(userJson);
                            return UserJson.fromEntity(
                                    new UserdataUserDaoJdbc(connection).create(user));
                        },
                        CFG.userDataJdbcUrl())
        );
    }
}