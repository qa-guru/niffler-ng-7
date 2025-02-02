package student.data.dao;

import student.data.entity.auth.AuthUserEntity;
import student.data.entity.auth.AuthorityEntity;

public interface AuthorityDao {
    AuthorityEntity createUser(AuthorityEntity authorityEntity);
    void deleteUser(AuthUserEntity authUserEntity);
}
