package student.data.entity.user;

import lombok.Getter;
import lombok.Setter;
import student.model.CurrencyValues;
import student.model.UserJson;

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

    private String fullname;

    private byte[] photo;

    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson json) {
        UserEntity user = new UserEntity();
        user.username = json.name();
        return user;
    }
}
