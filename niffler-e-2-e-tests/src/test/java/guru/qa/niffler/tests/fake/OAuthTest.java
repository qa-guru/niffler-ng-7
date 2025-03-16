package guru.qa.niffler.tests.fake;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.UserJson;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class OAuthTest {

    @Test
    @ApiLogin(username = "duck", password = "12345")
    void oauthTest(@Token String token, UserJson user) {
        System.out.println(user);
        Assertions.assertNotNull(token);
    }


    @ApiLogin(
            username = "Artur",
            password = "12345"
    )
    @DisplayName("Активная категория должна присутствовать и отображаться в списке категорий")
    @Test
    void checkSpendsAndCategoriesAndFriendsAndAllPeoplesFromExistUser(@NotNull UserJson user) {
        System.out.print(user.testData());
    }
}
