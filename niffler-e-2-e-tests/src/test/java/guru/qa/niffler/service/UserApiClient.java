package guru.qa.niffler.service;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.TokenResponse;
import guru.qa.niffler.model.UserJson;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Collections;
import java.util.List;

import static guru.qa.niffler.utils.OAuthUtils.extractCodeFromUrl;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient implements UsersClient {

    private static final Config CONFIG = Config.getInstance();
    private static final String PASSWORD = "123";
    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String REDIRECT_URI = CONFIG.frontUrl() + "authorized";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CODE_CHALLENGE_METHOD = "S256";


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(
                    new CookieManager(
                            ThreadSafeCookieStore.INSTANCE,
                            CookiePolicy.ACCEPT_ALL
                    )
            ))
            .build();

    public void authorize(String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                            RESPONSE_TYPE,
                            CLIENT_ID,
                            SCOPE,
                            REDIRECT_URI,
                            codeChallenge,
                            CODE_CHALLENGE_METHOD)
                    .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public String login(String username, String password) {
        final Response<Void> response;
        try {
            authApi.requestRegisterForm().execute();
            response = authApi.login(
                    username,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return extractCodeFromUrl(response.raw().request().url().toString());
    }

    public String token(String code, String codeVerifier) {
        final Response<TokenResponse> response;
        try {
            response = authApi.token(
                    CLIENT_ID,
                    REDIRECT_URI,
                    GRANT_TYPE,
                    code,
                    codeVerifier
            ).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        assert response.body() != null;
        return response.body().tokenId();
    }


    private final Retrofit retrofitAuth = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CONFIG.authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final Retrofit retrofitUserdata = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CONFIG.userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final AuthApi authApi = retrofitAuth.create(AuthApi.class);
    private final UserdataApi userdataApi = retrofitUserdata.create(UserdataApi.class);


    @Nonnull
    @Override
    public UserJson createUser(String username, String password) {
        try {
            authApi.requestRegisterForm().execute();
            authApi.register(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();
            UserJson createdUser = requireNonNull(userdataApi.currentUser(username).execute().body());
            return createdUser.addTestData(
                    new TestData(
                            password
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, PASSWORD);

                    response = userdataApi.sendInvitation(
                            newUser.username(),
                            targetUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .incomeInvitations()
                        .add(newUser);
            }
        }
    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, PASSWORD);

                    response = userdataApi.sendInvitation(
                            targetUser.username(),
                            newUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .outcomeInvitations()
                        .add(newUser);
            }
        }
    }

    @Override
    public void addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                try {
                    userdataApi.sendInvitation(
                            createUser(
                                    username,
                                    PASSWORD
                            ).username(),
                            targetUser.username()
                    ).execute();
                    response = userdataApi.acceptInvitation(targetUser.username(), username).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .friends()
                        .add(response.body());
            }
        }
    }

    @Nonnull
    public List<UserJson> allUsers(String username) {
        final Response<List<UserJson>> response;
        try {
            response = userdataApi.allUsers(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ?
                response.body()
                : Collections.emptyList();
    }
}