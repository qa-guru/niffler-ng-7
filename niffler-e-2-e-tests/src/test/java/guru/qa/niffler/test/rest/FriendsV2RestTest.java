package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pageble.RestResponsePage;
import guru.qa.niffler.service.GatewayV2ApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@RestTest
public class FriendsV2RestTest {
    private final GatewayV2ApiClient gatewayApiClient = new GatewayV2ApiClient();

    @RegisterExtension
    private final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    @ApiLogin
    @User(friends = 2, incomeInvitations = 1)
    @Test
    void friendsAndIncomeInvitationsListShouldBeReturnedWithFiltrationBySearchQuery(UserJson user,
                                                                                    @Token String token) {
        final UserJson testFriend = user.testData().friends().getFirst();
        final RestResponsePage<UserJson> response = gatewayApiClient.allFriends(token, 0, 2, null, testFriend.username());

        assertEquals(1, response.getContent().size());

        final UserJson foundedFriend = response.getContent().getFirst();
        assertSame(FriendshipStatus.FRIEND, foundedFriend.friendshipStatus());
        assertEquals(testFriend.id(), foundedFriend.id());
        assertEquals(testFriend.username(), foundedFriend.username());
    }
}