package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.PageInfo;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) throws IOException {
    CurrentUserRequest request = new CurrentUserRequest();
    request.setUsername(user.username());
    UserResponse response = userdataSoapClient.currentUser(request);
    Assertions.assertEquals(
        user.username(),
        response.getUser().getUsername()
    );
  }

  @Test
  @User(friends = 3)
  void allFriendsShouldBeReturnedInPageResponse(UserJson user) throws IOException {
    FriendsPageRequest request = new FriendsPageRequest();
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(0);
    pageInfo.setSize(10);
    request.setPageInfo(pageInfo);
    request.setUsername(user.username());

    UsersResponse response = userdataSoapClient.friendsPageable(request);
    Assertions.assertEquals(
        3,
        response.getUser().size()
    );
  }
}
