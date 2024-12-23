package quru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import quru.qa.niffler.config.Config;
import quru.qa.niffler.helper.UserDataHelper;
import quru.qa.niffler.model.UserJson;
import quru.qa.niffler.page.LoginPage;
import quru.qa.niffler.page.RegisterPage;

public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void shouldRegisterNewUser() {
        UserJson randomUser = UserDataHelper.getRandomUser();

        doUserRegistration(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeSuccessRegistrationText();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        UserJson randomUser = UserDataHelper.getRandomUser();

        doUserRegistration(randomUser.username(), randomUser.password(), randomUser.password());
        doUserRegistration(randomUser.username(), randomUser.password(), randomUser.password())
                .shouldSeeUsernameAlreadyExistErrorText(randomUser.username());
    }


    @Test
    void shouldNShowErrorIfPasswordAndConfirmPasswordAreNotEquals() {
        doUserRegistration(UserDataHelper.getRandomUser().username(),
                UserDataHelper.getRandomUser().password(),
                UserDataHelper.getRandomUser().password())
                .shouldSeePasswordsShouldBeEqualErrorText();
    }


    private static RegisterPage doUserRegistration(String username, String password, String passwordSubmit) {
        return Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(passwordSubmit)
                .submitRegistration();
    }

}
