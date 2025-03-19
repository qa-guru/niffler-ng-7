package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pageble.RestResponsePage;
import guru.qa.niffler.service.GatewayApiClient;
import guru.qa.niffler.service.GatewayV2ApiClient;
import guru.qa.niffler.service.UserApiClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestTest
public class FriendsRestTest {
    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final GatewayV2ApiClient gatewayV2ApiClient = new GatewayV2ApiClient();
    private final UsersClient usersClient = new UserApiClient();

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();
    ;


    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void userAndIncomeInvitationListShouldBeReturned(UserJson user, @Token String token) {
        final UserJson expectedFriend = user.testData().friends().getFirst();
        final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();
        List<UserJson> response = gatewayApiClient.allFriends(token, null);
        assertEquals(2, response.size());

        final UserJson actualFriend = response.getLast();
        final UserJson actualInvitation = response.getFirst();

        assertEquals(expectedFriend.id(), actualFriend.id());
        assertEquals(expectedInvitation.id(), actualInvitation.id());
    }

    @ApiLogin
    @User(friends = 7, incomeInvitations = 3)
    @Test
    void searchFriendShouldReturnUserByFilteredName(UserJson user, @Token String token) {
        final UserJson searchedFriend = user.testData().friends().getFirst();

        final RestResponsePage<UserJson> response = gatewayV2ApiClient.allFriends(
                token,
                0,
                5,
                null,
                searchedFriend.username()
        );

        final UserJson actualFriend = response.getContent().getFirst();

        assertEquals(1, response.getNumberOfElements());
        assertEquals(searchedFriend.id(), actualFriend.id());
        assertEquals(searchedFriend.username(), actualFriend.username());
    }

    @ApiLogin
    @User(friends = 2)
    @Test
    void friendShipShouldBeDeleted(UserJson user, @Token String token) {
        final UserJson friendToRemove = user.testData().friends().getFirst();
        gatewayApiClient.removeFriend(token, friendToRemove.username());

        final RestResponsePage<UserJson> response = gatewayV2ApiClient.allFriends(
                token,
                0,
                5,
                null,
                friendToRemove.username()
        );

        assertEquals(0, response.getNumberOfElements());
    }

    @ApiLogin
    @User(incomeInvitations = 2)
    @Test
    void friendsInvitationShouldAccepted(UserJson user, @Token String token) {
        UserJson friendToAccept = user.testData().incomeInvitations().getFirst();
        UserJson response = gatewayApiClient.acceptInvitation(token, friendToAccept);

        assertEquals(friendToAccept.id(), response.id());
        assertEquals(friendToAccept.username(), response.username());
        assertEquals(FriendshipStatus.FRIEND, response.friendshipStatus());
    }

    @ApiLogin
    @User(incomeInvitations = 2)
    @Test
    void friendsInvitationShouldDeclined(UserJson user, @Token String token) {
        UserJson friendToAccept = user.testData().incomeInvitations().getFirst();
        UserJson response = gatewayApiClient.declineInvitation(token, friendToAccept);

        assertEquals(friendToAccept.id(), response.id());
        assertEquals(friendToAccept.username(), response.username());
        assertEquals(null, response.friendshipStatus());
    }

    @ApiLogin
    @User
    @Test
    void incomeAndOutcomeInvitationsAShouldBeAfterFriendRequest(UserJson user, @Token String token) {

        UserJson friend = new UserJson(RandomDataUtils.randomUsername());

        final UserJson addressee = usersClient.createUser(
                friend.username(),
                "123"
        );
        UserJson sentInvitation = gatewayApiClient.sendInvitation(token, friend);
        final List<UserJson> incomeInvitationsActual = usersClient.allUsers(addressee.username());
        UserJson incomeInvitation = null;
        for (UserJson u : incomeInvitationsActual) {
            if (u.username().equals(user.username())) {
                incomeInvitation = u;
            }
        }

        assertEquals(addressee.username(), sentInvitation.username());
        assertEquals(user.username(), incomeInvitation.username());
    }
}