package student.data.dao;

import student.data.entity.auth.AuthUserEntity;

public interface AuthDao {
    AuthUserEntity createUser(AuthUserEntity user);
    void deleteUser(AuthUserEntity user);
}
