package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final Config CONFIG = Config.getInstance();

    private final EntityManager entityManager = em(CONFIG.authJdbcUrl());

    @Override
    public AuthUserEntity createUser(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUserEntity) {
        entityManager.joinTransaction();
        AuthUserEntity mergedUser = entityManager.merge(authUserEntity);
        entityManager.createQuery("DELETE FROM AuthorityEntity a WHERE a.user = :user")
                .setParameter("user", mergedUser)
                .executeUpdate();
        for (AuthorityEntity authority : authUserEntity.getAuthorities()) {
            authority.setUser(mergedUser);
            entityManager.persist(authority);
        }
        return mergedUser;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(AuthUserEntity.class, id)
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("SELECT u FROM AuthUserEntity u WHERE u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return entityManager.createQuery("SELECT u FROM AuthUserEntity u", AuthUserEntity.class)
                .getResultList();
    }

    @Override
    public void remove(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM AuthorityEntity a WHERE a.user = :user")
                .setParameter("user", user)
                .executeUpdate();
        entityManager.remove(user);
    }
}