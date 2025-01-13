package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDAO {

    List<AuthorityEntity> createUser(AuthUserEntity user);

}
