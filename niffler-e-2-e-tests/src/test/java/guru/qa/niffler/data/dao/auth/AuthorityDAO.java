package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

public interface AuthorityDAO {
    void create(AuthorityEntity... authority);
    void delete(AuthorityEntity... authority);
}
