package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {

    private static final Config CONFIG = Config.getInstance();

    private final SpendApi spendApi = new RestClient.EmtyRestClient(CONFIG.spendUrl()).create(SpendApi.class);

    @Override
    @Nonnull
    @Step("Создание траты через API")
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return requireNonNull(response.body());
    }

    @Override
    @Nonnull
    @Step("Создание категории через API")
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        CategoryJson result = requireNonNull(response.body());

        return category.archived()
                ? updateCategory(
                new CategoryJson(
                        result.id(),
                        result.name(),
                        result.username(),
                        true
                )
        ) : result;
    }

    @Override
    @Nonnull
    @Step("Обновление категории через API")
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Override
    @Step("Удаление категории через API")
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Unacceptable action - remove category using API");
    }

    @Step("Получение всех категорий для пользователя {username}")
    @Nonnull
    public List<CategoryJson> getAllCategories(String username) {
        final Response<List<CategoryJson>> response;

        try {
            response = spendApi.getAllCategories(username,false)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        assertEquals(200, response.code());

        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            return Collections.emptyList();
        }
    }

    @Step("Получение всех трат для пользователя {username}")
    @Nonnull
    public List<SpendJson> getAllSpends(String username) {
        final Response<List<SpendJson>> response;

        try {
            response = spendApi.getAllSpend(
                            username,
                            null,
                            null,
                            null
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        assertEquals(200, response.code());

        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            return Collections.emptyList();
        }
    }
}