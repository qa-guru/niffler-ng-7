package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.*;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserSoapClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SoapTest
public class SoapUsersTest {

    private final UserSoapClient userSoapClient = new UserSoapClient();

    @Test
    @User
    void currentUserTest(UserJson userJson) throws IOException {
        CurrentUserRequest request = new CurrentUserRequest();
        request.setUsername(userJson.username());
        UserResponse userResponse = userSoapClient.currentUser(request);

        assertEquals(
                userJson.username(),
                userResponse.getUser().getUsername()
        );
    }

    @Test
    @User
    void allUsersTest(UserJson userJson) throws IOException {
        AllUsersPageRequest request = new AllUsersPageRequest();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setSize(10);
        request.setPageInfo(pageInfo);
        request.setUsername(userJson.username());
        UsersResponse userResponse = userSoapClient.allUsers(request);

        assertEquals(
                10,
                userResponse.getUser().size()
        );
    }

    @Test
    @User(friends = 3)
    void allFriendsSortByUsernameTest(UserJson userJson) throws IOException {
        String friend = userJson.testData().friends().getFirst().username();
        FriendsPageRequest request = new FriendsPageRequest();
        request.setUsername(userJson.username());
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(0);
        pageInfo.setSize(5);
        request.setPageInfo(pageInfo);
        request.setSearchQuery(friend);
        UsersResponse userResponse = userSoapClient.allFriends(request);

        assertEquals(1, userResponse.getUser().size());
        assertEquals(friend,
                userResponse.getUser().getFirst().getUsername());
    }

    @Test
    @User(friends = 2)
    void removeFriendTest(UserJson userJson) throws IOException {
        String friendToRemove = userJson.testData().friends().getFirst().username();
        RemoveFriendRequest request = new RemoveFriendRequest();
        request.setUsername(userJson.username());
        request.setFriendToBeRemoved(friendToRemove);
        userSoapClient.removeFriend(request);

        FriendsPageRequest friendsPageRequest = new FriendsPageRequest();
        friendsPageRequest.setUsername(userJson.username());
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(0);
        pageInfo.setSize(5);
        friendsPageRequest.setPageInfo(pageInfo);
        friendsPageRequest.setSearchQuery(friendToRemove);
        UsersResponse userResponse = userSoapClient.allFriends(friendsPageRequest);

        assertEquals(0, userResponse.getUser().size());
    }

    @Test
    @User(incomeInvitations = 1)
    void addFriendTest(UserJson userJson) throws IOException {
        String friendToAdd = userJson.testData().incomeInvitations().getFirst().username();
        AcceptInvitationRequest request = new AcceptInvitationRequest();
        request.setUsername(userJson.username());
        request.setFriendToBeAdded(friendToAdd);
        UsersResponse response = userSoapClient.addFriend(request);

        assertEquals(1, response.getUser().size());
        assertEquals(friendToAdd, response.getUser().getFirst().getUsername());
    }

    @Test
    @User(incomeInvitations = 1)
    void declineFriendInvitationTest(UserJson userJson) throws IOException {
        String friend = userJson.testData().incomeInvitations().getFirst().username();
        DeclineInvitationRequest request = new DeclineInvitationRequest();
        request.setUsername(userJson.username());
        request.setInvitationToBeDeclined(friend);
        UsersResponse usersResponse = userSoapClient.declineFriendInvitation(request);

        assertEquals(FriendshipStatus.VOID, usersResponse.getUser().getFirst().getFriendshipStatus());
    }

    @Test
    @User()
    void sendFriendInvitationTest(UserJson userJson) throws IOException {

        SendInvitationRequest request = new SendInvitationRequest();
        request.setUsername(userJson.username());
        request.setFriendToBeRequested("taty");
        UsersResponse usersResponse = userSoapClient.sendFriendInvitation(request);

        assertEquals(FriendshipStatus.INVITE_SENT, usersResponse.getUser().getFirst().getFriendshipStatus());
    }
}
