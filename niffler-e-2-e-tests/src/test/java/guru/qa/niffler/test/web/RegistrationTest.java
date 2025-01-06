package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {

    private static final Config CFG = Config.getInstance();
    private static final String SUCCESS_REGISTRATION_MSG_TEXT = "Congratulations! You've registered!";
    private static final String USERNAME_ALREADY_EXISTS_ERROR_MSG_TEXT = "Username `%s` already exists";
    private static final String PW_SHOULD_BE_EQUAL_ERROR_MSG_TEXT = "Passwords should be equal";
    private static final String INCORRECT_PW_LENGTH_ERROR_MSG_TEXT = "Allowed password length should be from 3 to 12 characters";

    @Test
    public void shouldRegisterNewUser() {
        String username = RandomDataUtils.randomUsername();
        String pw = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openSignUpPage()
                .setUserName(username)
                .setPassword(pw)
                .setPasswordSubmit(pw)
                .submitRegistration()
                .checkThatSuccessMsgContains(SUCCESS_REGISTRATION_MSG_TEXT)
                .openLoginPage()
                .doLogin(username, pw)
                .checkIsLoaded();
    }

    @Test
    public void shouldNotRegisterUserWithExistingUsername() {
        String username = "duck";
        String pw = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openSignUpPage()
                .setUserName(username)
                .setPassword(pw)
                .setPasswordSubmit(pw)
                .submitRegistration()
                .checkThatErrorMessageContainsText(String.format(USERNAME_ALREADY_EXISTS_ERROR_MSG_TEXT, username));
    }

    @Test
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = RandomDataUtils.randomUsername();
        String pw = RandomDataUtils.randomPassword(3, 12);
        String pwSubmit = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openSignUpPage()
                .setUserName(username)
                .setPassword(pw)
                .setPasswordSubmit(pwSubmit)
                .submitRegistration()
                .checkThatErrorMessageContainsText(PW_SHOULD_BE_EQUAL_ERROR_MSG_TEXT);
    }

    @Test
    public void shouldShowErrorIfPasswordLengthIsNotCorrect() {
        String username = RandomDataUtils.randomUsername();
        String pw = RandomDataUtils.randomPassword(13, 20);
        String pwSubmit = RandomDataUtils.randomPassword(13, 20);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openSignUpPage()
                .setUserName(username)
                .setPassword(pw)
                .setPasswordSubmit(pwSubmit)
                .submitRegistration()
                .checkThatErrorMessageContainsText(INCORRECT_PW_LENGTH_ERROR_MSG_TEXT);
    }

}

