package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.Authority;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;


import java.util.UUID;


public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        Authority authority,
        @JsonProperty("user")
        AuthUserEntity user
) {


    public static AuthorityJson fromEntity(AuthorityEntity authority) {
        return new AuthorityJson(
                authority.getId(),
                authority.getAuthority(),
                authority.getUser()
        );
    }
}
