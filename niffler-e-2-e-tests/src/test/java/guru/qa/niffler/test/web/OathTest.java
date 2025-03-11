package guru.qa.niffler.test.web;

import guru.qa.niffler.service.UserApiClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.OAuthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.OAuthUtils.generateCodeVerifier;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OathTest {

    private final UserApiClient usersApiClient = new UserApiClient();

    @Test
    void authTest() {
        final String codeVerifier = generateCodeVerifier();
        final String codeChallenge = generateCodeChallenge(codeVerifier);

        usersApiClient.authorize(codeChallenge);
        String code = usersApiClient.login("taty", "123");
        String token = usersApiClient.token(code, codeVerifier);
        assertNotNull(token);
    }
}