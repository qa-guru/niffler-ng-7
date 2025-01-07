package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTest {

    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = "maria";
    private static final String PW = "12345";
    private static final String BAD_CREDENTIALS_ERROR_MSG = "Bad credentials";

    @Test
    public void mainPageShouldBeDisplayedAfterSuccessfulLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(USERNAME, PW)
                .checkIsLoaded();
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String pw = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(USERNAME)
                .setPassword(pw)
                .checkThatUserIsOnLoginPage();
    }

    @Test
    public void shouldShowErrorIfPasswordIsIncorrect() {
        String pw = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(USERNAME)
                .setPassword(pw)
                .submitCredentials()
                .errorMessageShouldContainText(BAD_CREDENTIALS_ERROR_MSG);
    }

    @Test
    public void shouldShowErrorIfUserDoesNotExist() {
        String username = RandomDataUtils.randomUsername();
        String pw = RandomDataUtils.randomPassword(3, 12);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(username)
                .setPassword(pw)
                .submitCredentials()
                .errorMessageShouldContainText(BAD_CREDENTIALS_ERROR_MSG);
    }

}
