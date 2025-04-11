package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    assertEquals(2, response.size());

    final UserJson actualInvitation = response.getFirst();
    final UserJson actualFriend = response.getLast();

    assertEquals(expectedFriend.id(), actualFriend.id());
    assertEquals(expectedInvitation.id(), actualInvitation.id());
  }

  @ApiLogin
  @User(friends = 2, outcomeInvitations = 1)
  @Test
  void friendsAndIncomeInvitationsListShouldBeReturnedWithFiltrationBySearchQuery(UserJson user,
                                                                                  @Token String token) {
    final UserJson testFriend = user.testData().friends().getFirst();

    final List<UserJson> response = gatewayApiClient.allFriends(token, testFriend.username());

    assertEquals(1, response.size());

    final UserJson foundedFriend = response.getFirst();

    assertSame(FriendshipStatus.FRIEND, foundedFriend.friendshipStatus());
    assertEquals(testFriend.id(), foundedFriend.id());
    assertEquals(testFriend.username(), foundedFriend.username());
  }

  @ApiLogin
  @User(friends = 1)
  @Test
  void frienshipShouldBeRemovedByAction(UserJson user,
                                        @Token String bearerToken) throws Exception {
    final String friendUsername = user.testData().friends().getFirst().username();
    gatewayApiClient.removeFriend(bearerToken, friendUsername);

    assertTrue(
        gatewayApiClient.allFriends(bearerToken, null).isEmpty(),
        "Current user should not have friend after removing"
    );
  }
}
