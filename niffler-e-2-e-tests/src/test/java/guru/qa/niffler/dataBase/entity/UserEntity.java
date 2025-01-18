package guru.qa.niffler.dataBase.entity;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;

    private String username;

    private CurrencyValues currency;

    private String firstname;

    private String surname;

    private byte[] photo;

    private byte[] photoSmall;

    private String fullname;

    @NotNull
    public static UserEntity fromJson(@NotNull UserJson userJson) {
        UserEntity ce = new UserEntity();
        ce.setId(userJson.id());
        ce.setUsername(userJson.username());
        ce.setCurrency(userJson.currency());
        ce.setFirstname(userJson.firstname());
        ce.setSurname(userJson.surname());
        ce.setPhoto(userJson.photo());
        ce.setPhotoSmall(userJson.photoSmall());
        ce.setFullname(userJson.fullname());
        return ce;
    }

}
