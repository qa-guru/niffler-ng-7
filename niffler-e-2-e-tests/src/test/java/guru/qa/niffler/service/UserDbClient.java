package guru.qa.niffler.service;


import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.utils.DataUtils.randomUserName;


public class UserDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositorySpringJdbc();

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userDataUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity auth = authUserEntity(username, password);
                    authUserRepository.create(auth);
                    return UserJson.fromEntity(
                            userdataUserRepository.create(userEntity(username)
                            )
                    );
                }
        );

    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id())
                    .orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            final String username = randomUserName();
                            userdataUserRepository.sendInvitation(
                                    createNewUser(username, "123"),
                                    targetEntity
                            );
                            return null;
                        }
                );
            }
        }
    }


    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id())
                    .orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            final String username = randomUserName();
                            userdataUserRepository.getInvitation(targetEntity,
                                    createNewUser(username, "123")
                            );
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTxTemplate.execute(() -> {
                            String username = randomUserName();
                            userdataUserRepository.addFriend(
                                    targetEntity,
                                    createNewUser(username, "12345")
                            );
                            return null;
                        }
                );
            }
        }
    }

    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return userdataUserRepository.create(userEntity(username));
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
}