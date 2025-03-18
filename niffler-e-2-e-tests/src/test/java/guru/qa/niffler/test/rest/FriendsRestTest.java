package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.GatewayApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestTest
public class FriendsRestTest {
    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @RegisterExtension
    private final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void userAndIncomeInvitationListShouldBeReturned(UserJson user, @Token String token){
        final UserJson expectedFriend = user.testData().friends().getFirst();
        final UserJson expectedInvitation= user.testData().incomeInvitations().getFirst();
        List<UserJson> response = gatewayApiClient.allFriends(token, null);
        assertEquals(2, response.size());

        final UserJson actualFriend = response.getLast();
        final UserJson actualInvitation= response.getFirst();

        assertEquals(expectedFriend.id(), actualFriend.id());
        assertEquals(expectedInvitation.id(), actualInvitation.id());
    }
}