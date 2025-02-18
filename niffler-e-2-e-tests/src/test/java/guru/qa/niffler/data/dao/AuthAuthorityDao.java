package guru.qa.niffler.data.dao;


import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthAuthorityDao {

    void createAuthority(AuthorityEntity... authority);

    List<AuthorityEntity> update(AuthorityEntity... authority);

    List<AuthorityEntity> findAll();

    List<AuthorityEntity> findAllByUserId(UUID userId);

    void remove(AuthorityEntity... authority);
}