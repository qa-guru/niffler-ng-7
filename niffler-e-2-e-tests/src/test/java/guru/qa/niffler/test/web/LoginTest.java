package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


public class LoginTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String userName = "taty";
        String password = "123";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(userName, password)
                .checkSuccessLogin();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String userName = "taty";
        String badPassword = "12345";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUserName(userName)
                .setPassword(badPassword)
                .checkBadCredentialError();
    }
}