package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.*;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUserSpringJdbc(UserJson userJson){
        AuthUserEntity auth = new AuthUserEntity();
        auth.setUsername(userJson.username());
        auth.setPassword(pe.encode("123"));
        auth.setEnabled(true);
        auth.setAccountNonExpired(true);
        auth.setAccountNonLocked(true);
        auth.setCredentialsNonExpired(true);

         AuthUserEntity result = new AuthUserDaoSpringJbc(dataSource(CFG.authJdbcUrl()))
                .createUser(auth);


        AuthorityEntity[] useAuthorities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(result.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .createAuthority(useAuthorities);



        return UserJson.fromEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userDataJdbcUrl()))
                        .create(
                                UserEntity.fromJson(
                                        userJson
                                )
                        ));
    }

    public Record createUser(AuthUserJson authUser, UserJson userJson) {
        return xaTransaction(
                new XaFunction<>(
                        connection -> {
                            AuthUserEntity auth = AuthUserEntity.fromJson(authUser);
                            AuthUserJson.fromEntity(
                                    new AuthUserDaoJdbc(connection).createUser(auth));
                            new AuthAuthorityDaoJdbc(connection).createAuthority(
                                    Arrays.stream(Authority.values())
                                            .map(a -> {
                                                        AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setId(auth.getId());
                                                        ae.setAuthority(a);
                                                        return ae;
                                                    }
                                            ).toArray(AuthorityEntity[]::new));
                            return null;

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