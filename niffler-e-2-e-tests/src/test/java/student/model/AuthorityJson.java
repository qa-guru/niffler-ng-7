package student.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import student.data.entity.auth.Authority;
import student.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("userId")
        UUID userId,
        @JsonProperty("authority")
        Authority authority
) {
    public static AuthorityJson fromEntity(AuthorityEntity entity, AuthUserJson authUserJson) {
        return new AuthorityJson(
                entity.getId(),
                authUserJson.id(),
                entity.getAuthority()
        );
    }
}