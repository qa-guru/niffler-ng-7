package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class ApiClient {
    private static Retrofit retrofit;

    private ApiClient() {   }

    public static Retrofit getINSTANCE() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if(retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Config.getInstance().spendUrl())
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
