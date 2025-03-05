package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDAOJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthorityDAOJdbc;
import guru.qa.niffler.data.dao.userdata.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserdataUserJson;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.UUID;
import java.util.function.Function;

import static guru.qa.niffler.data.DataBases.transaction;
import static guru.qa.niffler.data.DataBases.xaTransaction;

public class UserdataDBClient {
    private static final Config CFG = Config.getInstance();

    public UserdataUserJson create(AuthUserJson authUserJson, UserdataUserJson userdataJson) {
        return xaTransaction(Connection.TRANSACTION_READ_UNCOMMITTED,
                new DataBases.XaFunction<UserdataUserJson>(connection -> {
                    AuthUserEntity authUser = new AuthUserDAOJdbc(connection)
                            .create(AuthUserEntity.fromJson(authUserJson));
                    authUser.getAuthorities().forEach(x -> x.setUser(authUser));
                    new AuthorityDAOJdbc(connection).create(authUser);
                    return null;
                }, CFG.authJdbcUrl()),
                new DataBases.XaFunction<UserdataUserJson>(connection ->
                        UserdataUserJson.fromEntity(new UserdataUserDAOJdbc(connection)
                                .create(UserdataUserEntity.fromJson(userdataJson))),
                        CFG.userdataJdbcUrl()));
    }

    public @Nullable UserdataUserJson findById(UUID id) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, UserdataUserJson>) connection ->
                        new UserdataUserDAOJdbc(connection).findById(id)
                                .map(UserdataUserJson::fromEntity).orElse(null),
                CFG.userdataJdbcUrl());
    }

    public @Nullable UserdataUserJson findByUsername(String username) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, UserdataUserJson>) connection ->
                        new UserdataUserDAOJdbc(connection).findByUsername(username).
                                map(UserdataUserJson::fromEntity).orElse(null),
                CFG.userdataJdbcUrl());
    }

    public void delete(UserdataUserJson userdataUserJson) {
        xaTransaction(Connection.TRANSACTION_SERIALIZABLE,
                new DataBases.XaConsumer<>(connection ->
                        new UserdataUserDAOJdbc(connection)
                                .delete(UserdataUserEntity.fromJson(userdataUserJson)),
                        CFG.userdataJdbcUrl()),
                new DataBases.XaConsumer<>(connection -> {
                    AuthUserDAOJdbc userDAOJdbc = new AuthUserDAOJdbc(connection);
                    userDAOJdbc.findByUsername(userdataUserJson.username())
                            .ifPresent(x -> {
                                new AuthorityDAOJdbc(connection).delete(x);
                                userDAOJdbc.delete(x);
                            });
                },
                        CFG.authJdbcUrl()));
    }
}