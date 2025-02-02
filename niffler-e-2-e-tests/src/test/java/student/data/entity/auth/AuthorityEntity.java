package student.data.entity.auth;

import lombok.Getter;
import lombok.Setter;
import student.model.AuthorityJson;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {

    private UUID id;

    private Authority authority;

    private UUID user;

    public static AuthorityEntity fromJson(AuthorityJson authorityJson, AuthUserEntity authUserEntity) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setId(authorityJson.id());
        authorityEntity.setUser(authUserEntity.getId());
        authorityEntity.setAuthority(authorityJson.authority());
        return authorityEntity;
    }
}
