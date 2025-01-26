package student.api;

import retrofit2.Response;
import student.model.CurrencyValues;
import student.model.Period;
import student.model.SpendJson;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SpendApiClient {
    private final SpendApi spendApi;

    public SpendApiClient() {
        this.spendApi = ApiClient.getInstance().create(SpendApi.class);
    }

    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code(), "Создание траты не успешное");
        return response.body();
    }

    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Изменение траты не успешное");
        return response.body();
    }

    public SpendJson getSpend(String spendId) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(spendId)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Получение траты не успешное");
        return response.body();
    }

    public List<SpendJson> getSpends(CurrencyValues currency, Period period) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(currency, period)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Получение траты не успешное");
        return response.body();
    }

    public void removeSpend(List<String> spendIds) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpend(spendIds)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Не удалось удалить трату");
    }
}
