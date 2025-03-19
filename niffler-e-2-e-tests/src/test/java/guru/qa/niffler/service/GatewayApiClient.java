package guru.qa.niffler.service;

import guru.qa.niffler.api.GatewayApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = create(GatewayApi.class);
    }

    @Step("Отправляем GET запрос на api/friends/all")
    @Nonnull
    public List<UserJson> allFriends(String bearerToken, @Nullable String searchQuery) {
        Response<List<UserJson>> response;
        try {
            response = gatewayApi.allFriends(bearerToken, searchQuery).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return requireNonNull(response.body());
    }

    @Step("Отправляем DELETE запрос на api/friends/remove")
    public void removeFriend(String bearerToken, @Nullable String targetUsername) {
        try {
            gatewayApi.removeFriend(bearerToken, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Step("Отправляем POST запрос на api/invitations/accept")
    public UserJson acceptInvitation(String bearerToken, UserJson friend) {
        Response<UserJson> response;
        try {
            response = gatewayApi.acceptInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Отправляем POST запрос на api/invitations/decline")
    public UserJson declineInvitation(String bearerToken, UserJson friend) {
        Response<UserJson> response;
        try {
            response = gatewayApi.declineInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Отправляем POST запрос на api/invitations/send")
    public UserJson sendInvitation(String bearerToken, UserJson friend) {
        Response<UserJson> response;
        try {
            response = gatewayApi.sendInvitation(bearerToken, friend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

}

