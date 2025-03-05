package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class AuthUserEntity implements Serializable {
    private UUID id;

    private String username;

    private String password;

    private Boolean enabled;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private List<AuthorityEntity> authorities = new ArrayList<>();

    public static AuthUserEntity fromJson(AuthUserJson authUserJson) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setId(authUserJson.id());
        authUserEntity.setUsername(authUserJson.username());
        authUserEntity.setPassword(authUserJson.password());
        authUserEntity.setEnabled(authUserJson.enabled());
        authUserEntity.setAccountNonExpired(authUserJson.accountNonExpired());
        authUserEntity.setAccountNonLocked(authUserJson.accountNonLocked());
        authUserEntity.setCredentialsNonExpired(authUserJson.credentialsNonExpired());
        authUserEntity.setAuthorities(authUserJson.authorities());
        return authUserEntity;
    }
}
