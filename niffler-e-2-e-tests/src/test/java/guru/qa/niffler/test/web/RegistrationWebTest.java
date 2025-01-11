package guru.qa.niffler.test.web;

import org.junit.jupiter.api.Test;
import guru.qa.niffler.helper.UserDataHelper;
import guru.qa.niffler.model.UserJson;

import static guru.qa.niffler.page.RegisterPage.register;

public class RegistrationWebTest {

    @Test
    void shouldRegisterNewUser() {
        UserJson randomUser = UserDataHelper.getRandomUser();

        register(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeSuccessRegistrationText();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        UserJson randomUser = UserDataHelper.getRandomUser();

        register(randomUser.username(), randomUser.password(), randomUser.password());
        register(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeUsernameAlreadyExistErrorText(randomUser.username());
    }


    @Test
    void shouldNShowErrorIfPasswordAndConfirmPasswordAreNotEquals() {
        register(UserDataHelper.getRandomUser().username(),
                UserDataHelper.getRandomUser().password(),
                UserDataHelper.getRandomUser().password())
                .shouldSeePasswordsShouldBeEqualErrorText();
    }


}
