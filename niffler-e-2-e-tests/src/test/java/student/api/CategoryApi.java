package student.api;

import student.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

interface CategoryApi {

    @POST("/internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @PATCH("/internal/categories/update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

    @GET("/internal/categories/all")
    Call<List<CategoryJson>> getCategories(@Query("excludeArchived") Boolean excludeArchived);
}
