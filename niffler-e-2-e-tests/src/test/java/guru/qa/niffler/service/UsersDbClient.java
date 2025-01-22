package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDAO userdataUserDAO = new UserdataUserDAOJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword("12345");
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

                    AuthAuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthAuthorityEntity ae = new AuthAuthorityEntity();
                                ae.setUser_id(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthAuthorityEntity[]::new);

                    authAuthorityDao.createAuthority(authorityEntities);
                    return UserJson.fromEntity(
                            userdataUserDAO.createUser(UserEntity.fromJson(user))
                    );
                }
        );
    }

}
