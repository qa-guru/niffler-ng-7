package guru.qa.niffler.service;


import guru.qa.jaxb.userdata.*;
import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class UserSoapClient extends RestClient {

    private static final Config CONFIG = Config.getInstance();
    private final UserdataSoapApi userdataSoapApi;

    public UserSoapClient() {
        super(CONFIG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
        userdataSoapApi = create(UserdataSoapApi.class);
    }


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(
                    new CookieManager(
                            ThreadSafeCookieStore.INSTANCE,
                            CookiePolicy.ACCEPT_ALL
                    )
            ))
            .build();

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


    @Nonnull
    public UserResponse currentUser(CurrentUserRequest request) throws IOException {
        return Objects.requireNonNull(userdataSoapApi.currentUser(request).execute().body());
    }

    @Nonnull
    public UsersResponse allUsers(AllUsersPageRequest request) throws IOException {
       return Objects.requireNonNull(userdataSoapApi.allUsers(request).execute().body());
    }

    @Nonnull
    public UsersResponse allFriends(FriendsPageRequest request) throws IOException {
        return Objects.requireNonNull(userdataSoapApi.allFriends(request).execute().body());
    }

    public void removeFriend(RemoveFriendRequest request) throws IOException {
     userdataSoapApi.removeFriend(request).execute();
    }

    @Nonnull
    public UsersResponse addFriend(AcceptInvitationRequest request) throws IOException {
       return Objects.requireNonNull(userdataSoapApi.addFriend(request).execute().body());
    }

    @Nonnull
    public UsersResponse declineFriendInvitation(DeclineInvitationRequest request) throws IOException {
        return Objects.requireNonNull(userdataSoapApi.declineFriend(request).execute().body());
    }

    @Nonnull
    public UsersResponse sendFriendInvitation(SendInvitationRequest request) throws IOException {
        return Objects.requireNonNull(userdataSoapApi.inviteFriend(request).execute().body());
    }
}
