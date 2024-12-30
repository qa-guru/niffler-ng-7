package api;

import model.CategoryJson;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryApiClient {

    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        this.categoryApi = ApiClient.getInstance().create(CategoryApi.class);
    }

    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = categoryApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Создание категории не успешное");
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
        assertEquals(200, response.code(), "Обновление категории не успешное");
        return response.body();
    }

    public List<CategoryJson> getCategories(Boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = categoryApi.getCategories(excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Получений категорий не успешное");
        return response.body();
    }
}
