package guru.qa.niffler.dataBase.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.dataBase.dbConnection.DataBases;
import guru.qa.niffler.dataBase.entity.*;
import guru.qa.niffler.dataBase.impl.*;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import guru.qa.niffler.model.UserJson;

import javax.sql.DataSource;
import java.util.Arrays;

import static guru.qa.niffler.dataBase.dbConnection.DataBases.*;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public Record createUser(AuthUserJson authUser, UserJson userJson) {
        return xaTransaction(1,
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
    public UserJson createUserSpringJdbc(AuthUserJson authUserJson,UserJson userJson){

        AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUserJson);
        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJDBCUrl()))
                .createUser(authUserEntity);
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setId(createdAuthUser.getId());
        authorityEntity.setAuthority(Authority.write);

        AuthorityEntity[] userAuthorities =  Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setId(createdAuthUser.getId());
                    authority.setAuthority(e);
                    return authority;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthorityDaoSpringJdbc(dataSource(CFG.authJDBCUrl())).createUser(authorityEntity);
        return UserJson.fromEntity(
                new UseDataDaoSpringJdbc(dataSource(CFG.userdataJDBCUrl()))
                        .createUser(
                                UserEntity.fromJson(userJson)
                        )
        );
    }
}
