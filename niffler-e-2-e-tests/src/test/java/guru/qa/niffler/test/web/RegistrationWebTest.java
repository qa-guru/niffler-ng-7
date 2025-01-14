package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import org.junit.jupiter.api.Test;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.page.RegisterPage.register;

@WebTest
public class RegistrationWebTest {

    @Test
    void shouldRegisterNewUser() {
        UserJson randomUser = RandomDataUtils.getRandomUser();

        register(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeSuccessRegistrationText();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        UserJson randomUser = RandomDataUtils.getRandomUser();

        register(randomUser.username(), randomUser.password(), randomUser.password());
        register(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeUsernameAlreadyExistErrorText(randomUser.username());
    }


    @Test
    void shouldNShowErrorIfPasswordAndConfirmPasswordAreNotEquals() {
        register(RandomDataUtils.getRandomUser().username(),
                RandomDataUtils.getRandomUser().password(),
                RandomDataUtils.getRandomUser().password())
                .shouldSeePasswordsShouldBeEqualErrorText();
    }


}
