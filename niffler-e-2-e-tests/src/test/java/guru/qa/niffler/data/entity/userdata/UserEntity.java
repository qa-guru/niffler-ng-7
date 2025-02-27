package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson userJson) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userJson.id());
        userEntity.setUsername(userJson.username());
        userEntity.setFirstname(userJson.firstname());
        userEntity.setSurname(userJson.firstname());
        userEntity.setFullname(userJson.fullname());
        userEntity.setPhoto(userJson.photo().getBytes());
        userEntity.setPhotoSmall(userJson.photoSmall().getBytes());
        return userEntity;
    }
}