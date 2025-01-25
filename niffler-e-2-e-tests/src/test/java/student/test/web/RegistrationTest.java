package student.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import student.config.Config;
import student.jupiter.annotaion.meta.WebTest;
import student.jupiter.extension.browser.BrowserExtension;
import student.pages.LoginPage;
import student.util.DataGenerator;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        var user = DataGenerator.getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSubmitButton()
                .verifySuccessfulRegistration()
                .clickLoginButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .clickSubmitButton()
                .verifyTitleMainPage();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        var user = DataGenerator.getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSubmitButton()
                .verifySuccessfulRegistration()
                .clickLoginButton()
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .verifyErrorAlreadyExistsUserMessage(user.name());
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        var user = DataGenerator.getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.name())
                .verifyErrorPasswordNotEqualMessage();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        var user = DataGenerator.getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSubmitButton()
                .verifySuccessfulRegistration()
                .clickLoginButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .clickSubmitButton()
                .verifyTitleMainPage();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        var user = DataGenerator.getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSubmitButton()
                .verifySuccessfulRegistration()
                .clickLoginButton()
                .setUserName(user.name())
                .setPassword(user.name())
                .verifyErrorCredentialsMessage()
                .verifyLoginPage();
    }
}
