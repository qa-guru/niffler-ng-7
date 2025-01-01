package guru.qa.niffler.helpers.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendingJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {


    private final Retrofit retrofit;


    private final SpendApi addSpendApi;

    public SpendApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://127.0.0.1:8093")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        addSpendApi = retrofit.create(SpendApi.class);

    }

    public SpendingJson createSpend(SpendingJson spendingJson) {
        final Response<SpendingJson> response;
        try {
            response = addSpendApi.addSpend(spendingJson)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = addSpendApi.getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = addSpendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = addSpendApi.createCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public void deleteSpends(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = addSpendApi.deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
    }

    public List<SpendingJson> getAllSpends(String username, CurrencyValues filterCurrency, String from, String to) {
        final Response<List<SpendingJson>> response;
        try {
            response = addSpendApi.getAllSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public SpendingJson getSpend(String id, String username) {
        final Response<SpendingJson> response;
        try {
            response = addSpendApi.getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }

    public SpendingJson editSpend(SpendingJson spendJson) {
        final Response<SpendingJson> response;
        try {
            response = addSpendApi.editSpend(spendJson)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code(), "Код ответ некорректный");
        return response.body();
    }
}
