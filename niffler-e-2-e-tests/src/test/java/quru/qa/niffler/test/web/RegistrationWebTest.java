package quru.qa.niffler.test.web;

import org.junit.jupiter.api.Test;
import quru.qa.niffler.helper.UserDataHelper;
import quru.qa.niffler.model.UserJson;

import static quru.qa.niffler.page.RegisterPage.register;

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
