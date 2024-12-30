package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<CategoryJson> getCategories(@Query("username") String username,
                                   @Query("excludeArchived") boolean excludeArchived);
}
