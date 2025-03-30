package guru.qa.niffler.test.fake;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OathTest {

    @Test
    @ApiLogin(username = "taty", password = "123")
    void authTest(@Token String token, UserJson user) {
        System.out.println(user);
        assertNotNull(token);
    }
}