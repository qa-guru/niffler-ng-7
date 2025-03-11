package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.DataBases;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDAOJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthorityDAOJdbc;
import guru.qa.niffler.data.dao.userdata.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.model.UserdataUserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

import static guru.qa.niffler.data.DataBases.transaction;
import static guru.qa.niffler.data.DataBases.xaTransaction;

public class UserdataDBClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserdataUserJson createBySpring(UserdataUserJson user) {
        return UserdataUserJson.fromEntity(
                xaTransaction(Connection.TRANSACTION_READ_UNCOMMITTED,
                        new DataBases.XaFunction<UserdataUserEntity>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDAOJdbc(con).create(authUser);
                                    new AuthorityDAOJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new DataBases.XaFunction<UserdataUserEntity>(
                                con -> {
                                    UserdataUserEntity ue = new UserdataUserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDAOJdbc(con).create(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ),
                null);
    }

    public UserdataUserJson createUser(UserdataUserJson user) {
        return UserdataUserJson.fromEntity(
                xaTransaction(Connection.TRANSACTION_READ_UNCOMMITTED,
                        new DataBases.XaFunction<UserdataUserEntity>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDAOJdbc(con).create(authUser);
                                    new AuthorityDAOJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new DataBases.XaFunction<UserdataUserEntity>(
                                con -> {
                                    UserdataUserEntity ue = new UserdataUserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDAOJdbc(con).create(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ),
                null);
    }

    public @Nullable UserdataUserJson findById(UUID id) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, UserdataUserJson>) connection ->
                        new UserdataUserDAOJdbc(connection).findById(id)
                                .map(x -> UserdataUserJson.fromEntity(x, null)).orElse(null),
                CFG.userdataJdbcUrl());
    }

    public @Nullable UserdataUserJson findByUsername(String username) {
        return transaction(Connection.TRANSACTION_READ_COMMITTED,
                (Function<Connection, UserdataUserJson>) connection ->
                        new UserdataUserDAOJdbc(connection).findByUsername(username)
                                .map(x -> UserdataUserJson.fromEntity(x, null)).orElse(null),
                CFG.userdataJdbcUrl());
    }

    public void delete(UserdataUserJson userdataUserJson) {

    }
}