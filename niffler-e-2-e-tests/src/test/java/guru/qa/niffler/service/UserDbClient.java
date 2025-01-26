package guru.qa.niffler.service;


import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;


public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataUrl()
    );


    private final TransactionTemplate chainyTxTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),

                    new ChainedTransactionManager(
                            new JdbcTransactionManager(DataSources.dataSource(CFG.userDataJdbcUrl()))
                    )
            )
    );

    public UserJson createUserSpringJdbc(UserJson userJson) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity auth = new AuthUserEntity();
                    auth.setUsername(userJson.username());
                    auth.setPassword(pe.encode("123"));
                    auth.setEnabled(true);
                    auth.setAccountNonExpired(true);
                    auth.setAccountNonLocked(true);
                    auth.setCredentialsNonExpired(true);

                    AuthUserEntity result = authUserDao.createUser(auth);

                    AuthorityEntity[] useAuthorities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(result.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.createAuthority(useAuthorities);
                    return UserJson.fromEntity(
                            userdataUserDao.create(
                                    UserEntity.fromJson(userJson)
                            )
                    );
                }
        );

    }

    public UserJson createUserWithChainTx(UserJson user) {
        return chainyTxTemplate.execute(status -> {
            AuthUserEntity auth = new AuthUserEntity();
            auth.setUsername(user.username());
            auth.setPassword(pe.encode("123"));
            auth.setEnabled(true);
            auth.setAccountNonExpired(true);
            auth.setAccountNonLocked(true);
            auth.setCredentialsNonExpired(true);

            AuthUserEntity result = authUserDao.createUser(auth);

            AuthorityEntity[] useAuthorities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(null);
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);

            authAuthorityDao.createAuthority(useAuthorities);

            return UserJson.fromEntity(
                    userdataUserDao.create(
                            UserEntity.fromJson(user)
                    ));
        });
    }

    public List<AuthUserEntity> findAllUsers() {
        return xaTxTemplate.execute(() -> authUserDao.findAll());
    }

}