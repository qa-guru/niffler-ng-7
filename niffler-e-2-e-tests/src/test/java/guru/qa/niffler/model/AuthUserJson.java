package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthUserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password,
        @JsonProperty("enable")
        Boolean enable,
        @JsonProperty("accountNonExpired")
        Boolean accountNonExpired,
        @JsonProperty("accountNonLocked")
        Boolean accountNonLocked,
        @JsonProperty("credentialsNonExpired")
        Boolean credentialsNonExpired,
        @JsonProperty("authorities")
        List<AuthorityEntity> authorities)
{

    public static AuthUserJson fromEntity(AuthUserEntity authUser) {
        return new AuthUserJson(
                authUser.getId(),
                authUser.getUsername(),
                authUser.getPassword(),
                authUser.getEnabled(),
                authUser.getAccountNonExpired(),
                authUser.getAccountNonLocked(),
                authUser.getCredentialsNonExpired(),
                authUser.getAuthorities()
        );
    }
}
