package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.pageable.RestResponsePage;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@RestTest
public class FriendsV2RestTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

  private final GatewayV2ApiClient gatewayApiClient = new GatewayV2ApiClient();

  @ApiLogin
  @User(friends = 1, incomeInvitations = 1)
  @Test
  void friendsAndIncomeInvitationsListShouldBeReturned(UserJson user, @Token String token) {
    final UserJson expectedFriend = user.testData().friends().getFirst();
    final UserJson expectedInvitation = user.testData().incomeInvitations().getFirst();

    final RestResponsePage<UserJson> response = gatewayApiClient.allFriends(token, 0, 2, null, null);

    Assertions.assertEquals(2, response.getContent().size());

    final UserJson actualInvitation = response.getContent().getFirst();
    final UserJson actualFriend = response.getContent().getLast();

    Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
    Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
  }

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
