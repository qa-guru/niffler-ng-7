package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.Authority;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
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
