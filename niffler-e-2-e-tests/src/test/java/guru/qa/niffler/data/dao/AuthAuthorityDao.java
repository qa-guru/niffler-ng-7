package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

    void createUser(AuthAuthorityEntity... authAuthorityEntity);

    List<AuthAuthorityEntity> findAll();

    List<AuthAuthorityEntity> findAllByUserId(UUID userId);
}
