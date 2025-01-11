package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.helper.UserDataHelper;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;

public class LoginWebTest {

    private static final Config CFG = Config.getInstance();


    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("test", "test")
                .checkThatMainPageVisible();
    }


    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        UserJson randomUser = UserDataHelper.getRandomUser();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(randomUser.username(),
                        randomUser.password());
        new LoginPage().shouldSeeErrorWithBadCredentialsText();
    }


}
