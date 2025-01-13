package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.UserJson;
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

    public void addAuthorities(AuthorityEntity... authorities) {
        for (AuthorityEntity authority : authorities) {
            this.authorities.add(authority);
            authority.setUser(this);
        }
    }

    public void removeAuthority(AuthorityEntity authority) {
        this.authorities.remove(authority);
        authority.setUser(null);
    }

    public static AuthUserEntity fromJson(UserJson userJson) {
        AuthUserEntity aue = new AuthUserEntity();

        aue.setId(null);
        aue.setUsername(userJson.username());
        aue.setPassword(userJson.authUserJson().password());
        aue.setEnabled(userJson.authUserJson().enabled());
        aue.setAccountNonExpired(userJson.authUserJson().accountNonExpired());
        aue.setAccountNonLocked(userJson.authUserJson().accountNonLocked());
        aue.setCredentialsNonExpired(userJson.authUserJson().credentialsNonExpired());

        AuthorityEntity readAuthorityEntity = new AuthorityEntity();
        readAuthorityEntity.setAuthority(Authority.read);
        AuthorityEntity writeAuthorityEntity = new AuthorityEntity();
        writeAuthorityEntity.setAuthority(Authority.write);
        aue.addAuthorities(readAuthorityEntity, writeAuthorityEntity);

        return aue;
    }

}
