package guru.qa.niffler.dataBase.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.*;
import guru.qa.niffler.dataBase.impl.AuthUserDaoJdbc;
import guru.qa.niffler.dataBase.impl.AuthorityDaoJdbc;
import guru.qa.niffler.dataBase.impl.UseDataDaoJdbc;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import guru.qa.niffler.model.UserJson;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public Record createUser(AuthUserJson authUser, UserJson userJson) {
        return DataBases.xaTransaction(1,
                new DataBases.XaFunction<>(
                        connection -> {
                            AuthUserEntity auth = AuthUserEntity.fromJson(
                                    authUser
                            );
                            return AuthUserJson.fromEntity(
                                    new AuthUserDaoJdbc(connection).createUser(auth)
                            );
                        }, CFG.authJDBCUrl()),
                new DataBases.XaFunction<>(
                        connection -> {
                            UserEntity user = UserEntity.fromJson(
                                    userJson
                            );
                            return UserJson.fromEntity(
                                    new UseDataDaoJdbc(connection).createUser(user)
                            );
                        }, CFG.userdataJDBCUrl())

        );
    }
}
