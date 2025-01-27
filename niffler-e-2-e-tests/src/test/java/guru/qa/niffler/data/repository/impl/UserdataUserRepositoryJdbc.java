package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config config = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());

            ps.executeUpdate();

            final UUID generateKey;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generateKey = rs.getObject("id", UUID.class);

                } else {
                    throw new SQLException("Can not find id in ResultSet");
                }
            }
            user.setId(generateKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.ofNullable(UserdataUserEntityRowMapper.instance.mapRow(rs, 1));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(config.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.ofNullable(UserdataUserEntityRowMapper.instance.mapRow(rs, 1));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
