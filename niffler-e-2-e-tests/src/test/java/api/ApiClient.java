package api;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://127.0.0.1:8093/";
    private static Retrofit retrofit;

    private ApiClient() {}

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
