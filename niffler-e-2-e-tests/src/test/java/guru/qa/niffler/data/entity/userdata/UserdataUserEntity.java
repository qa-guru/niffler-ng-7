package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserdataUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
public class UserdataUserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserdataUserEntity fromJson(UserdataUserJson userJson) {
        String photo = userJson.photo();
        String photoSmall = userJson.photoSmall();
        UserdataUserEntity userdataUserEntity = new UserdataUserEntity();
        userdataUserEntity.setId(userJson.id());
        userdataUserEntity.setUsername(userJson.username());
        userdataUserEntity.setCurrency(userJson.currency());
        userdataUserEntity.setFirstname(userJson.firstname());
        userdataUserEntity.setSurname(userJson.surname());
        userdataUserEntity.setFullname(userJson.fullname());
        userdataUserEntity.setPhoto(photo != null ? photo.getBytes(StandardCharsets.UTF_8) : null);
        userdataUserEntity.setPhotoSmall(photoSmall != null ? photoSmall.getBytes(StandardCharsets.UTF_8) : null);
        return userdataUserEntity;
    }
}