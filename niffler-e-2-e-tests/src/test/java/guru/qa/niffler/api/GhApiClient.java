package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;

import java.util.Objects;

import static guru.qa.niffler.api.ApiClient.GH_API;

public class GhApiClient {
    private final GhApi ghApi = GH_API.getINSTANCE().create(GhApi.class);

    private static final String GH_TOKEN_ENV = "Niffler7";

    @SneakyThrows
    public String issueState(String issueNumber) {
        JsonNode responce = ghApi.issue(
                "Bearer " + System.getenv(GH_TOKEN_ENV), issueNumber)
                .execute()
                .body();
    return Objects.requireNonNull(responce).get("state").asText();
    }
}
