package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Isolated
public class GetUsersTest {

    private final UserApiClient usersApiClient = new UserApiClient();

    @Order(Integer.MAX_VALUE)
    @Test
    @User
    void checkNotEmptyUsersList(UserJson user) {
        List<UserJson> users = usersApiClient.allUsers(user.username());
        assertFalse(users.isEmpty());
    }

    @Order(1)
    @Test
    @User
    void checkEmptyUsersList(UserJson user) {
        List<UserJson> users = usersApiClient.allUsers(user.username());
        assertTrue(users.isEmpty());
    }
}
