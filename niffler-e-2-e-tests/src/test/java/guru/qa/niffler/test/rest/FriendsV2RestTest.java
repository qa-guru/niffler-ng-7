package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pageble.RestResponsePage;
import guru.qa.niffler.service.GatewayV2ApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestTest
public class FriendsV2RestTest {
    private final GatewayV2ApiClient gatewayApiClient = new GatewayV2ApiClient();

    @RegisterExtension
    private final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void userAndIncomeInvitationListShouldBeReturned(UserJson user, @Token String token) {
        final UserJson expectedFriend = user.testData().friends().getFirst();
        final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();
        RestResponsePage<UserJson> response = gatewayApiClient.allFriends(token, 0, 10, null, null);
        assertEquals(2, response.getContent().size());

        final UserJson actualFriend = response.getContent().getLast();
        final UserJson actualInvitation = response.getContent().getFirst();

        assertEquals(expectedFriend.id(), actualFriend.id());
        assertEquals(expectedInvitation.id(), actualInvitation.id());
    }
}