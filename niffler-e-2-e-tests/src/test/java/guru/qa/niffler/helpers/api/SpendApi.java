package guru.qa.niffler.helpers.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendingJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface SpendApi {

    @POST("/internal/spends/add")
    Call<SpendingJson> addSpend(@Body SpendingJson json);

    @POST("/internal/categories/add")
    Call<CategoryJson> createCategory(@Body CategoryJson category);

    @PATCH("/internal/spends/edit")
    Call<SpendingJson> editSpend(@Body SpendingJson spendJson);

    @GET("/internal/spends/{id}")
    Call<SpendingJson> getSpend(@Path("id") String id,
                             @Query("username") String username);
    @GET("/internal/categories/all")
    Call<List<CategoryJson>> getCategories(
            @Query("username") String username,
            @Query("excludeArchived") boolean excludeArchived);

    @DELETE("/internal/spends/remove")
    Call<Void> deleteSpends(
            @Query("username") String username,
            @Query("ids") List<String> ids);

    @PATCH("/internal/categories/update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

    @GET("/internal/spends/all")
    Call<List<SpendingJson>> getAllSpends(
            @Query("username") String username,
            @Query("filterCurrency") CurrencyValues filterCurrency,
            @Query("from") String from,
            @Query("to") String to);
}
