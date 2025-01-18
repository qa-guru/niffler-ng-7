package guru.qa.niffler.dataBase.dao;

import guru.qa.niffler.dataBase.entity.AuthUserEntity;
import guru.qa.niffler.dataBase.entity.AuthorityEntity;
import guru.qa.niffler.dataBase.entity.SpendEntity;

import java.util.List;

public interface AuthorityDao {
    AuthorityEntity createUser(AuthorityEntity authority);
    List<AuthorityEntity> findAll();
}
