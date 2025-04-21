package guru.qa.niffler.service;

import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UserResponse;
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
        return userdataSoapApi.currentUser(request).execute().body();

    }

}