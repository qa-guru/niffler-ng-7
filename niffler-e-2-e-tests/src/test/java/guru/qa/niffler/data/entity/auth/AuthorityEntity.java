package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.AuthorityJson;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Table(name = "authority")
public class AuthorityEntity implements Serializable {

    private UUID id;
    private Authority authority;
    private AuthUserEntity user;

    public static AuthorityEntity fromJson(AuthorityJson authority) {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setId(authority.id());
        ae.setAuthority(authority.authority());
        ae.setUser(authority.user());
        return ae;
    }
}