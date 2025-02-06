package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;


public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private final UserdataUserDAO userDaoJdbc = new UserdataUserDAOJdbc();

    @Override
    public UserEntity createUser(UserEntity user) {
        return userDaoJdbc.createUser(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userDaoJdbc.update(user);
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
        userDaoJdbc.update(requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userDaoJdbc.update(requester);
        userDaoJdbc.update(addressee);
    }

}
