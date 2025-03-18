package guru.qa.niffler.service;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pageble.RestResponsePage;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayV2ApiClient extends RestClient {

    private final GatewayV2Api gatewayApi;

    public GatewayV2ApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi=create(GatewayV2Api.class);
    }

    @Step("Отправляем GET запрос на api/v2/friends/all")
    @Nonnull
    public RestResponsePage<UserJson> allFriends(String bearerToken,
                                                 int page,
                                                 int size,
                                                 @Nullable String sort,
                                                 @Nullable String searchQuery){
        Response<RestResponsePage<UserJson> > response;
        try {
            response = gatewayApi.allFriends(bearerToken, page, size, sort, searchQuery).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());

    }
}