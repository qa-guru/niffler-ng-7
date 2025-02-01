package student.api;

import retrofit2.Call;
import retrofit2.http.*;
import student.model.CurrencyValues;
import student.model.Period;
import student.model.SpendJson;

import java.util.List;

public interface SpendApi {

    @POST("/internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @PATCH("/internal/spends/edit")
    Call<SpendJson> editSpend(@Body SpendJson spendJson);

    @GET("/internal/spends/{id}")
    Call<SpendJson> getSpend(@Query("id") String ids);

    @GET("/internal/spends/all")
    Call<List<SpendJson>> getSpends(@Query("filterCurrency") CurrencyValues currency, @Query("filterPeriod") Period period);

    @DELETE("/internal/spends/remove")
    Call<Void> removeSpend(@Query("ids") List<String> ids);
}
