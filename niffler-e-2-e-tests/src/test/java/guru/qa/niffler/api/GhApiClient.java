package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final GhApi ghApi;

    public GhApiClient() {
        super(CONFIG.ghUrl());
        this.ghApi = create(GhApi.class);
    }

    @Nonnull
    public String issueState(String issueNumber) {
        final JsonNode response;
        try {
            response = ghApi
                    .issue("Bearer " + System.getenv(GH_TOKEN_ENV),
                            issueNumber)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return Objects.requireNonNull(response).get("state").asText();
    }

}