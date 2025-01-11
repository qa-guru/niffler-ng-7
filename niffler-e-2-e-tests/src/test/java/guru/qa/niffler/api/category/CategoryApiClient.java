package guru.qa.niffler.api.category;


import okhttp3.OkHttpClient;
import org.apache.hc.core5.http.HttpStatus;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryApiClient {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.getInstance().categoryUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);

    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }
}
