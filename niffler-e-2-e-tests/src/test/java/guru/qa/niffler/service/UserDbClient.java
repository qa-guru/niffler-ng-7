    package guru.qa.niffler.service;


    import guru.qa.niffler.config.Config;
    import guru.qa.niffler.data.entity.auth.AuthUserEntity;
    import guru.qa.niffler.data.entity.auth.AuthorityEntity;
    import guru.qa.niffler.data.entity.userdata.UserEntity;
    import guru.qa.niffler.data.repository.AuthUserRepository;
    import guru.qa.niffler.data.repository.UserdataUserRepository;
    import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
    import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
    import guru.qa.niffler.data.tpl.DataSources;
    import guru.qa.niffler.data.tpl.XaTransactionTemplate;
    import guru.qa.niffler.model.Authority;
    import guru.qa.niffler.model.CurrencyValues;
    import guru.qa.niffler.model.UserJson;
    import org.springframework.data.transaction.ChainedTransactionManager;
    import org.springframework.jdbc.support.JdbcTransactionManager;
    import org.springframework.security.crypto.factory.PasswordEncoderFactories;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.transaction.support.TransactionTemplate;

    import java.util.Arrays;


    public class UserDbClient {

        private static final Config CFG = Config.getInstance();
        private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
        private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

        private final TransactionTemplate txTemplate = new TransactionTemplate(
                new JdbcTransactionManager(
                        DataSources.dataSource(CFG.authJdbcUrl())
                )
        );

        private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
                CFG.authJdbcUrl(),
                CFG.userDataUrl()
        );


        private final TransactionTemplate chainTxTemplate = new TransactionTemplate(
                new ChainedTransactionManager(
                        new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),

                        new ChainedTransactionManager(
                                new JdbcTransactionManager(DataSources.dataSource(CFG.userDataJdbcUrl()))
                        )
                )
        );

        public UserJson createUser(String username, String password) {
            return xaTxTemplate.execute(() -> {
                        AuthUserEntity auth = authUserEntity(username, password);
                        authUserRepository.createUser(auth);
                        return UserJson.fromEntity(
                                userdataUserRepository.create(userEntity(username)
                                )
                        );
                    }
            );

        }

        public UserEntity userEntity(String username) {
            UserEntity ue = new UserEntity();
            ue.setUsername(username);
            ue.setCurrency(CurrencyValues.RUB);
            return ue;
        }

        private AuthUserEntity authUserEntity(String username, String password) {
            AuthUserEntity auth = new AuthUserEntity();
            auth.setUsername(username);
            auth.setPassword(pe.encode(password));
            auth.setEnabled(true);
            auth.setAccountNonExpired(true);
            auth.setAccountNonLocked(true);
            auth.setCredentialsNonExpired(true);
            auth.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(auth);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            return auth;
        }

    //    public UserJson createUserWithChainTx(UserJson user) {
    //        return chainyTxTemplate.execute(status -> {
    //            AuthUserEntity auth = new AuthUserEntity();
    //            auth.setUsername(user.username());
    //            auth.setPassword(pe.encode("123"));
    //            auth.setEnabled(true);
    //            auth.setAccountNonExpired(true);
    //            auth.setAccountNonLocked(true);
    //            auth.setCredentialsNonExpired(true);
    //
    //            AuthUserEntity result = authUserRepository.createUser(auth);
    //
    //            AuthorityEntity[] useAuthorities = Arrays.stream(Authority.values()).map(
    //                    e -> {
    //                        AuthorityEntity ae = new AuthorityEntity();
    //                        ae.setUser(null);
    //                        ae.setAuthority(e);
    //                        return ae;
    //                    }
    //            ).toArray(AuthorityEntity[]::new);
    //
    //            ai.createAuthority(useAuthorities);
    //
    //            return UserJson.fromEntity(
    //                    userdataUserDao.create(
    //                            UserEntity.fromJson(user)
    //                    ));
    //        });
    //    }

    //    public List<AuthUserEntity> findAllUsers() {
    //        return xaTxTemplate.execute(() -> authUserDao.findAll());
    //    }

    }