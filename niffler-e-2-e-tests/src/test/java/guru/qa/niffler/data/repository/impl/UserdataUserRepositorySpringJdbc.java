package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDAO;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

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
    public UserEntity update(UserEntity user)  {
        return userDaoSpringJdbc.update(user);
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
        userDaoSpringJdbc.update(requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userDaoSpringJdbc.update(requester);
        userDaoSpringJdbc.update(addressee);
    }

}
