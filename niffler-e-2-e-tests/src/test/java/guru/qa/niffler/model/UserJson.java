package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
    @JsonProperty("id")
    UUID id,

    @JsonProperty("username")
    String username,

    @JsonProperty("firstname")
    String firstname,

    @JsonProperty("surname")
    String surname,

    @JsonProperty("fullname")
    String fullname,

    @JsonProperty("currency")
    CurrencyValues currency,

    @JsonProperty("photo")
    String photo,

    @JsonProperty("photoSmall")
    String photoSmall,

    @JsonProperty("auth")
    AuthUserJson authUserJson

) {

    public static UserJson fromAuthEntity(AuthUserEntity aue) {
        return new UserJson(
                aue.getId(),
                aue.getUsername(),
                null,
                null,
                null,
                null,
                null,
                null,
                AuthUserJson.fromEntity(aue)
        );
    }

    public static UserJson fromEntity(UserEntity ue) {
        return new UserJson(
                ue.getId(),
                ue.getUsername(),
                ue.getFirstname(),
                ue.getSurname(),
                ue.getFullname(),
                ue.getCurrency(),
                ue.getPhoto() != null && ue.getPhoto().length > 0 ? new String(ue.getPhoto(), StandardCharsets.UTF_8) : null,
                ue.getPhotoSmall() != null && ue.getPhotoSmall().length > 0 ? new String(ue.getPhotoSmall(), StandardCharsets.UTF_8) : null,
                null
        );
    }
}
