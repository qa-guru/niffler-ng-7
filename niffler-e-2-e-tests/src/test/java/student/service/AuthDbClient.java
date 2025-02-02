package student.service;

import student.config.Config;
import student.data.Databases;
import student.data.dao.impl.AuthDaoJdbc;
import student.data.dao.impl.AuthorityDaoJdbc;
import student.data.entity.auth.AuthUserEntity;
import student.data.entity.auth.AuthorityEntity;
import student.model.AuthUserJson;
import student.model.AuthorityJson;

import java.sql.SQLException;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    public AuthUserEntity createUser(AuthUserJson userJson, AuthorityJson authorityJson) throws SQLException {
        return Databases.transaction(connection -> {
            var authDaoJdbc = new AuthDaoJdbc(connection);
            var authorityDaoJdbc = new AuthorityDaoJdbc(connection);
            var createdUser = authDaoJdbc.createUser(AuthUserEntity.fromJson(userJson));
            authorityDaoJdbc.createUser(AuthorityEntity.fromJson(authorityJson, createdUser));
            return createdUser;
        }, CFG.authJdbcUrl(), null);
    }

    public void deleteUser(AuthUserEntity authUserEntity) {
        Databases.transaction(connection -> {
            var authDaoJdbc = new AuthDaoJdbc(connection);
            var authorityDaoJdbc = new AuthorityDaoJdbc(connection);
            authorityDaoJdbc.deleteUser(authUserEntity);
            authDaoJdbc.deleteUser(authUserEntity);
        }, CFG.authJdbcUrl(), null);
    }
}
