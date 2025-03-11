package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("id_token")
        String tokenId,
        String scope,
        @JsonProperty("expires_in")
        int expiresIn
        ) {
}
