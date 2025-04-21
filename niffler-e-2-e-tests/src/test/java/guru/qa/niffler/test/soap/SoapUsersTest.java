package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.meta.SoapTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserSoapClient;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.UserResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
