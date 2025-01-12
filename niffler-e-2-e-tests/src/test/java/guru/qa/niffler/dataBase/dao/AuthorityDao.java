package guru.qa.niffler.dataBase.dao;

import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;

public interface AuthorityDao {
    AuthorityEntity createUser(AuthorityEntity authority);
}
