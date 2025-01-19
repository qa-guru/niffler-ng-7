package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.api.ApiClient.SPEND_API;
import static java.lang.String.format;

public class CategoryApiClient {

    private final CategoryApi categoryApi = SPEND_API.getINSTANCE().create(CategoryApi.class);

    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.addCategories(category).execute();
        } catch (IOException e) {
            throw new AssertionError();
        }
        Assertions.assertEquals(200, response.code(), format("Категория %s не была создана", category.toString()));
        return response.body();
    }

    @PATCH("internal/categories/update")
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.updateCategories(category).execute();
        } catch (IOException e) {
            throw new AssertionError();
        }
        Assertions.assertEquals(200, response.code(), "Категория не была обновлена");
        return response.body();
    }

    @GET("internal/categories/all")
    public List<CategoryJson> getAllCategories(Boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = categoryApi.allCategories(excludeArchived).execute();
        } catch (IOException e) {
            throw new AssertionError();
        }
        Assertions.assertEquals(200, response.code(), "Получить все категории не удалось");
        return response.body();
    }
}