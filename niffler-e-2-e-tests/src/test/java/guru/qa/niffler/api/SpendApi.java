package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.Period;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getIDSpend(@Query("id") String id);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getAllSpend(@Query("filterPeriod") Period period,
                                    @Query("filterCurrency") CurrencyValues currency);

  @DELETE("internal/spends/remove")
  Call<Void> removeSpend(@Query("ids") List<String> ids);

}
