package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config config = Config.getInstance();
    private final UserdataUserDAO userDaoSpringJdbc = new UserdataUserDAOSpringJdbc();

    @Override
    public UserEntity createUser(UserEntity user) {
        return userDaoSpringJdbc.createUser(user);
    }

    @Override
    public void update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(config.userdataJdbcUrl()));
        jdbcTemplate.update(
                "UPDATE \"user\" " +
                        "SET currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ? " +
                        "WHERE id = ?",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getCurrency().name());
                    preparedStatement.setString(2, user.getFirstname());
                    preparedStatement.setString(3, user.getSurname());
                    preparedStatement.setBytes(4, user.getPhoto());
                    preparedStatement.setBytes(5, user.getPhotoSmall());
                    preparedStatement.setObject(6, user.getId());
                    preparedStatement.executeUpdate();
                }
        );
        List<FriendshipEntity> friendshipRequests = user.getFriendshipRequests();

        if (!friendshipRequests.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO friendship (requester_id, addressee_id, status) " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (requester_id, addressee_id)" +
                    " DO UPDATE SET status = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NotNull PreparedStatement preparedStatement, int i) throws SQLException {
                    FriendshipEntity friendshipEntity = friendshipRequests.get(i);
                    preparedStatement.setObject(1, user.getId());
                    preparedStatement.setObject(2, friendshipEntity.getAddressee().getId());
                    preparedStatement.setString(3, friendshipEntity.getStatus().name());
                    preparedStatement.setString(4, friendshipEntity.getStatus().name());
                }

                @Override
                public int getBatchSize() {
                    return friendshipRequests.size();
                }
            });
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDaoSpringJdbc.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
       return userDaoSpringJdbc.findByUsername(username);
    }

    @Override
    public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        update(requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        update(requester);
        update(addressee);
    }

}
