package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

public interface AuthorityDAO {
    void create(AuthUserEntity userAuth);
    void delete(AuthUserEntity authUserEntity);
}
