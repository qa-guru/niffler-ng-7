package guru.qa.niffler.dataBase.entity;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity extends AuthUserEntity implements Serializable {
  private UUID id;
  private Authority authority;
  private AuthUserEntity user;


  public static AuthorityEntity fromJson(AuthorityJson authorityJson) {
    AuthorityEntity ce = new AuthorityEntity();
    ce.setId(authorityJson.id());
    ce.setAuthority(authorityJson.authority());
    ce.setUser(authorityJson.user());
    return ce;
  }
}
