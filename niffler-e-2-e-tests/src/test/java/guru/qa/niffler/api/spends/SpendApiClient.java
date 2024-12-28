package guru.qa.niffler.api.spends;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private static final String BASE_URL = "http://127.0.0.1:8093";
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    public SpendJson getAllSpends(@Nonnull String username,
                                  @Nullable CurrencyValues filterCurrency,
                                  @Nullable String from,
                                  @Nullable String to) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpends(username, filterCurrency, from, to).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpendById(@Nonnull String id,
                                  @Nonnull String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson deleteSpend(@Nonnull String username, @Nonnull List<String> ids) {
        final Response<SpendJson> response;
        try {
            response = spendApi.deleteSpends(username, ids).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
