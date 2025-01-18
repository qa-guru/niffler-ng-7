package guru.qa.niffler.dataBase.entity;

import guru.qa.niffler.model.AuthUserJson;

import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;

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
    private List<AuthorityEntity> authorities;

    public static AuthUserEntity fromJson(AuthUserJson authUserJson) {
        AuthUserEntity ce = new AuthUserEntity();
        ce.setId(authUserJson.id());
        ce.setUsername(authUserJson.username());
        ce.setPassword(authUserJson.password());
        ce.setEnabled(authUserJson.enable());
        ce.setAccountNonExpired(authUserJson.accountNonExpired());
        ce.setAccountNonLocked(authUserJson.accountNonLocked());
        ce.setCredentialsNonExpired(authUserJson.credentialsNonExpired());
        ce.setAuthorities(authUserJson.authorities());
        return ce;
    }

}
