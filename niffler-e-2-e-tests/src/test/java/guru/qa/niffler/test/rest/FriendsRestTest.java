package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

@RestTest
public class FriendsRestTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

  @ApiLogin
  @User(friends = 1, incomeInvitations = 1)
  @Test
  void friendsAndIncomeInvitationsListShouldBeReturned(UserJson user, @Token String token) {
    final UserJson expectedFriend = user.testData().friends().getFirst();
    final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();

    final List<UserJson> response = gatewayApiClient.allFriends(token, null);

    Assertions.assertEquals(2, response.size());

    final UserJson actualInvitation = response.getFirst();
    final UserJson actualFriend = response.getLast();

    Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
    Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
  }
}
