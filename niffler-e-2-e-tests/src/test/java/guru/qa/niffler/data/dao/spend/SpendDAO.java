package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDAO {
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    Optional<SpendEntity> findAllByUsernameAndDescription(String username, String name);

    List<SpendEntity> findAll();

    List<SpendEntity> findAllByUsername(String username);

    void delete(SpendEntity spend);
}
