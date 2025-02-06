package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.createUser(user);
        authAuthorityDao.createAuthority(user.getAuthorities().toArray(new AuthorityEntity[0]));
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        authUserDao.update(user);
        authAuthorityDao.update(user.getAuthorities().toArray(AuthorityEntity[]::new));
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id);
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return authUserDao.findAll();
    }

    @Override
    public Optional<AuthUserEntity> findByUserName(String username) {
        return authUserDao.findByUserName(username);
    }

    @Override
    public void remove(AuthUserEntity user) {
        authAuthorityDao.remove(user.getAuthorities().toArray(AuthorityEntity[]::new));
        authUserDao.remove(user);
    }
}