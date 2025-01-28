package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config config = Config.getInstance();
    private final UserdataUserDAO userDaoJdbc = new UserdataUserDAOJdbc();

    @Override
    public UserEntity createUser(UserEntity user) {
        return userDaoJdbc.createUser(user);
    }

    @Override
    public void update(UserEntity user) {
        {
            try (PreparedStatement usersPs = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                    "UPDATE \"user\" " +
                            "SET currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ? " +
                            "WHERE id = ?");

                 PreparedStatement friendsPs = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                         "INSERT INTO friendship (requester_id, addressee_id, status) " +
                                 "VALUES (?, ?, ?) " +
                                 "ON CONFLICT (requester_id, addressee_id)" +
                                 " DO UPDATE SET status = ?")
            ) {
                usersPs.setString(1, user.getCurrency().name());
                usersPs.setString(2, user.getFirstname());
                usersPs.setString(3, user.getSurname());
                usersPs.setBytes(4, user.getPhoto());
                usersPs.setBytes(5, user.getPhotoSmall());
                usersPs.setObject(6, user.getId());
                usersPs.executeUpdate();

                for (FriendshipEntity fe : user.getFriendshipRequests()) {
                    friendsPs.setObject(1, user.getId());
                    friendsPs.setObject(2, fe.getAddressee().getId());
                    friendsPs.setString(3, fe.getStatus().name());
                    friendsPs.setString(4, fe.getStatus().name());
                    friendsPs.addBatch();
                    friendsPs.clearParameters();
                }
                friendsPs.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDaoJdbc.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDaoJdbc.findByUsername(username);
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
