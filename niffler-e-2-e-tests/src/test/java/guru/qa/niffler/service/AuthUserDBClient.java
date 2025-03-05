package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDAOJdbc;
import guru.qa.niffler.data.dao.userdata.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.UserdataUserJson;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.UUID;
import java.util.function.Function;

import static guru.qa.niffler.data.DataBases.transaction;

public class AuthUserDBClient {
    private static final Config CFG = Config.getInstance();

    public @Nullable AuthUserJson findByUsername(String username) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, AuthUserJson>) connection ->
                        new AuthUserDAOJdbc(connection).findByUsername(username)
                                .map(AuthUserJson::fromEntity).orElse(null),
                CFG.authJdbcUrl());
    }
}
