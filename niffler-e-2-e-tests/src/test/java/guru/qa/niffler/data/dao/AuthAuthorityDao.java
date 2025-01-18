package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDao {
    void createAuthority(AuthorityEntity... authorityEntities);

    List<AuthorityEntity> findAll();

}
