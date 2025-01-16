package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class AuthUserEntity implements Serializable {

    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private List<AuthorityEntity> authorities = new ArrayList<>();

    public static AuthUserEntity fromJson(AuthUserJson user) {
        AuthUserEntity ae = new AuthUserEntity();
        ae.setId(user.id());
        ae.setUsername(user.username());
        ae.setPassword(user.password());
        ae.setEnabled(user.enable());
        ae.setAccountNonExpired(user.accountNonExpired());
        ae.setAccountNonLocked(user.accountNonLocked());
        ae.setCredentialsNonExpired(user.credentialsNonExpired());
        ae.setCredentialsNonExpired(user.credentialsNonExpired());
        ae.setAuthorities(user.authorities());
        return ae;
    }
}