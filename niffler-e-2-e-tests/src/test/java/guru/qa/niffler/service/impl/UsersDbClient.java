package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UsersClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CONFIG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final String PASSWORD = "12345";

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CONFIG.authJdbcUrl(),
            CONFIG.userdataJdbcUrl()
    );

    @NotNull
    @Override
    public UserJson createUser(String username, String password) {
        return requireNonNull(
                xaTransactionTemplate.execute(
                        () -> UserJson.fromEntity(
                                createNewUser(username, password),
                                null
                        ).addTestData(
                                new TestData(
                                        password
                                )
                        )
                )
        );
    }

    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .incomeInvitations()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, PASSWORD);
                                                            userdataUserRepository.addFriendshipRequest(
                                                                    newUser,
                                                                    targetEntity
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                        FriendshipStatus.PENDING
                                )
                        );
            }
        }
    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .outcomeInvitations()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, PASSWORD);
                                                            userdataUserRepository.addFriendshipRequest(
                                                                    targetEntity,
                                                                    newUser
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                FriendshipStatus.PENDING
                                )
                        );
            }
        }
    }

    @Override
    public void addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .friends()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, PASSWORD);
                                                            userdataUserRepository.addFriend(
                                                                    targetEntity,
                                                                    newUser
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                FriendshipStatus.ACCEPTED
                                )
                        );
            }
        }
    }

    @NotNull
    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.createUser(authUser);
        return userdataUserRepository.createUser(userEntity(username));
    }

    @NotNull
    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @NotNull
    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}