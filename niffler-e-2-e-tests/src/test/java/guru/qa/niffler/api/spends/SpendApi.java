package guru.qa.niffler.api.spends;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nullable;
import java.util.List;


public interface SpendApi {

    @GET("/internal/spends/all")
    Call<SpendJson> getSpends(@Query("username") String username, @Query("filterCurrency") @Nullable CurrencyValues filterCurrency, @Query("from") @Nullable String from, @Query("to") @Nullable String to);

    @POST("/internal/spends/{id}")
    Call<SpendJson> getSpend(@Path("id") String id, @Query("username") String username);

    @POST("/internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @PATCH("/internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spend);

    @DELETE("/internal/spends/remove")
    Call<SpendJson> deleteSpends(@Query("username") String username, @Query("ids") List<String> ids);
}