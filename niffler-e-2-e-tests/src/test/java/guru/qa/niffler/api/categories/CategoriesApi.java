package guru.qa.niffler.api.categories;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nonnull;

public interface CategoriesApi {

    @GET("/internal/categories/all")
    Call<CategoryJson> getCategories(@Query("username") @Nonnull String username, boolean excludeArchived);

    @POST("/internal/categories/add")
    Call<CategoryJson> addCategory(@Body @Nonnull CategoryJson category);

    @PATCH("/internal/categories/update")
    Call<CategoryJson> updateCategory(@Body @Nonnull CategoryJson category);
}