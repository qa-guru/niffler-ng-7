package guru.qa.niffler.api;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.Period;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.api.ApiClient.SPEND_API;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final SpendApi spendApi = SPEND_API.getINSTANCE().create(SpendApi.class);

  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    assertEquals(201, response.code(), format("Трата [%s] не создана", spend) + response.message());
    return response.body();
  }

  public SpendJson getIDSpend(String id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getIDSpend(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    assertEquals(200, response.code(), format("Трата с id [%s] не найдена", id) + response.message());
    return response.body();
  }

  public SpendJson editSpend(String id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getIDSpend(id).execute();
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    assertEquals(200, response.code(), format("Трата с id [%s] не найдена", id) + response.message());
    return response.body();
  }

  public List<SpendJson> getAllSpends(Period period, CurrencyValues values) {
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getAllSpend(period, values).execute();
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    assertEquals(200, response.code(), "Получить траты не удалось: " + response.message());
    return response.body();
  }

  public void removeSpend(List<String> ids) {
    final Response<Void> response;
    try {
      response = spendApi.removeSpend(ids).execute();
    } catch (IOException e) {
      throw new AssertionError(e.getMessage());
    }
    assertEquals(200, response.code(), "Трата не удалена" + response.message());
  }
}
