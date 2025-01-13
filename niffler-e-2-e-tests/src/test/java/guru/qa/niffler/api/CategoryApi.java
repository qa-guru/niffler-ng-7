package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CategoryApi {
    @POST("internal/categories/add")
    Call<CategoryJson> addCategories(@Body CategoryJson spend);

    @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategories(@Body CategoryJson spend);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> allCategories(@Query("excludeArchived") Boolean excludeArchived);
}
