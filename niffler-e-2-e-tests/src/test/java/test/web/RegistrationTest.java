package test.web;

import com.codeborne.selenide.Selenide;
import config.Config;
import jupiter.browser.BrowserExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.LoginPage;

import static util.DataGenerator.getUser;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser(){
        var user = getUser();

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
    void shouldNotRegisterUserWithExistingUsername(){
        var user = getUser();

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
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
        var user = getUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(user.name())
                .setPassword(user.password())
                .setPasswordSubmit(user.name())
                .verifyErrorPasswordNotEqualMessage();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(){
        var user = getUser();

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
        var user = getUser();

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
