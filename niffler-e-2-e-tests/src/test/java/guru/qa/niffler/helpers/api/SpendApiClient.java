package guru.qa.niffler.helpers.api;

import guru.qa.niffler.model.SpendingJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendApiClient {


    private final Retrofit retrofit;


    private final AddSpendApi addSpendApi;

    public SpendApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://127.0.0.1:8090")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        addSpendApi = retrofit.create(AddSpendApi.class);
    }

    public SpendingJson createSpend(SpendingJson spendingJson) {
        try {
            return addSpendApi.addSpend(spendingJson)
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
