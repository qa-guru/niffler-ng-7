package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendApiClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8093/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @SneakyThrows
    public SpendJson createSpend(SpendJson spend) {
        return spendApi.addSpend(spend)
                .execute()
                .body();
    }
}