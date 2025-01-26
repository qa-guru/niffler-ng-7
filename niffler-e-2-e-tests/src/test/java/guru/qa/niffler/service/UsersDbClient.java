package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;
import static guru.qa.niffler.data.tpl.DataSources.dataSourceChained;


public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();


    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    private final TransactionTemplate txTemplateWithChainedTxManager = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSourceChained(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSourceChained(CFG.userdataJdbcUrl())
                    )
            )
    );


    public UserJson createCorrectUserSpringJdbc(UserJson user) {
        return xaTxTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(e -> {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setUserId(createdAuthUser.getId());
                authority.setAuthority(e);
                return authority;
            }).toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(userAuthorities);

            return UserJson.fromEntity(
                    udUserDao.create(UserEntity.fromJson(user)), null);
        });
    }

    public UserJson createIncorrectUserSpringJdbc(UserJson user) {
        return xaTxTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(e -> {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setUserId(createdAuthUser.getId());
                authority.setAuthority(e);
                return authority;
            }).toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(userAuthorities);

            UserEntity fromJson = UserEntity.fromJson(user);
            fromJson.setUsername(null);
            return UserJson.fromEntity(
                    udUserDao.create(fromJson), null);
        });
    }

    public UserJson createWithChainedTxManager(UserJson user) {

        return txTemplateWithChainedTxManager.execute(status -> {
                    AuthUserEntity aue = new AuthUserEntity();
                    aue.setUsername(user.username());
                    aue.setPassword(pe.encode("12345"));
                    aue.setEnabled(true);
                    aue.setAccountNonLocked(true);
                    aue.setAccountNonExpired(true);
                    aue.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(aue);

                    AuthorityEntity[] userAuthorities = Arrays.stream(Authority.values()).map(e -> {
                        AuthorityEntity authority = new AuthorityEntity();
                        authority.setUserId(createdAuthUser.getId());
                        authority.setAuthority(e);
                        return authority;
                    }).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(userAuthorities);

                    //3- создаем запись в табл user, niffler-userdata
                    UserEntity ue = udUserDao.create(UserEntity.fromJson(user));
                    return UserJson.fromEntity(ue, null);
                }
        );
    }

    public Optional<UserEntity> findByUsername(String username) {
        return xaTxTemplate.execute(() -> udUserDao.findByUsername(username));
    }
}
