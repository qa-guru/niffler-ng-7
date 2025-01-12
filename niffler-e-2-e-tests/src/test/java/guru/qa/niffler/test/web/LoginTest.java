package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.model.User.REGESTED_USERNAME;
import static guru.qa.niffler.util.GenerateUtils.generatePassword;

public class LoginTest {
    public static final Config CFG = Config.getInstance();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        User regestedUser = User.getRegestedUser();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(regestedUser.getUsername(), regestedUser.getPassword())
                .checkMainPageIsOpen();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        User userWithBadCredentials = new User(REGESTED_USERNAME, generatePassword());

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLoginWithBadCredentials(userWithBadCredentials.getUsername(), userWithBadCredentials.getPassword())
                .checkCredentialsError();
    }
}
