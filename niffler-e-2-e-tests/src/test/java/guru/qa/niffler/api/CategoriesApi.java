package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CategoriesApi {

        @POST("internal/categories/add")
        Call<CategoryJson> addCategories(@Body CategoryJson category);

        @PATCH("internal/categories/update")
        Call<CategoryJson> updateCategory(@Body CategoryJson category);

        @GET("internal/categories/all")
        Call<List<CategoryJson>> allCategories(@Query("username") String username);
}
