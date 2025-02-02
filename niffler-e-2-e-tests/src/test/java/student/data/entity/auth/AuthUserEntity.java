package student.data.entity.auth;

import lombok.Getter;
import lombok.Setter;
import student.model.AuthUserJson;

import java.io.Serializable;
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

    public static AuthUserEntity fromJson(AuthUserJson authUser) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setId(authUser.id());
        authUserEntity.setUsername(authUser.username());
        authUserEntity.setPassword(authUser.password());
        authUserEntity.setEnabled(authUser.enabled());
        authUserEntity.setAccountNonExpired(authUser.accountNonExpired());
        authUserEntity.setAccountNonLocked(authUser.accountNonLocked());
        authUserEntity.setCredentialsNonExpired(authUser.credentialsNonExpired());
        return authUserEntity;
    }
}
